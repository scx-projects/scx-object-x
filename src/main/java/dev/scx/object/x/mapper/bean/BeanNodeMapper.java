package dev.scx.object.x.mapper.bean;

import dev.scx.node.ObjectNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ConstructorInfo;
import dev.scx.reflect.FieldInfo;
import dev.scx.reflect.TypeInfo;

import java.lang.reflect.InvocationTargetException;

import static dev.scx.node.NullNode.NULL;
import static dev.scx.object.x.mapper.bean.BeanNodeMapperHelper.*;

/// 通用对象处理器
///
/// @author scx567888
public final class BeanNodeMapper implements TypeNodeMapper<Object, ObjectNode> {

    private static final BeanNodeMapperOptions BEAN_NODE_MAPPER_OPTIONS = new BeanNodeMapperOptions();

    private final ClassInfo classInfo;
    // 允许为 null, 因为有些 Bean 没有可用的无参构造函数
    private final ConstructorInfo defaultConstructor;
    // 可读的 field 列表.
    private final FieldInfo[] readableFields;
    // 可写的 field 列表.
    private final FieldInfo[] writableFields;
    // 性能优化: 缓存 writableFields 对应的 TypeNodeMapper, 避免在每个 Field 转换时重复执行 findMapper.
    private TypeNodeMapper<?, ?>[] writableFieldNodeMappers;

    BeanNodeMapper(ClassInfo classInfo) {
        this.classInfo = classInfo;
        // 这里我们只是获取一下这个 构造器, 并不进行 是否存在或是否可访问的校验,
        // 因为有时候 BeanNodeMapper 只会被用作 valueToNode 根本用不上 defaultConstructor, 所以这里延后校验以提供更强的容错性.
        this.defaultConstructor = classInfo.defaultConstructor();
        this.readableFields = filterReadableFields(this.classInfo);
        this.writableFields = filterWritableFields(this.readableFields);
        this.writableFieldNodeMappers = null;

        // 这里尝试开放反射访问权限, 失败忽略.
        trySetAccessible(this.defaultConstructor, this.readableFields);
    }

    private static Object getFieldValue(FieldInfo fieldInfo, Object value) throws ObjectToNodeException {
        try {
            return fieldInfo.get(value);
        } catch (IllegalAccessException e) {
            // 因为我们 使用的都是 public 字段 理论上不会出现 这种异常
            throw new ObjectToNodeException(e);
        }
    }

    private static void setFieldValue(FieldInfo fieldInfo, Object object, Object value) throws NodeToObjectException {
        try {
            fieldInfo.set(object, value);
        } catch (IllegalAccessException e) {
            // 因为我们 使用的都是 public 字段 理论上不会出现 这种异常
            throw new NodeToObjectException(e);
        }
    }

    @Override
    public TypeInfo valueType() {
        return classInfo;
    }

    @Override
    public Class<ObjectNode> nodeType() {
        return ObjectNode.class;
    }

    @Override
    public ObjectNode valueToNode(Object objectValue, ObjectToNodeContext context) throws ObjectToNodeException {
        var options = context.getMapperOptions(BeanNodeMapperOptions.class, BEAN_NODE_MAPPER_OPTIONS);
        var writePolicy = options.beanFieldWritePolicy();

        var objectNode = new ObjectNode();

        for (var fieldInfo : readableFields) {
            var name = fieldInfo.name();
            var value = getFieldValue(fieldInfo, objectValue);

            if (writePolicy != null) {
                var result = writePolicy.apply(fieldInfo, value);
                if (result != null) {
                    // 如果需要跳过
                    if (result.skip()) {
                        continue;
                    }
                    name = result.name();
                    value = result.value();
                }
            }

            var node = context.objectToNode(value, name);
            objectNode.put(name, node);
        }

        return objectNode;
    }

    @Override
    public Object nodeToValue(ObjectNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 0, 性能优化.
        if (writableFieldNodeMappers == null) {
            writableFieldNodeMappers = new TypeNodeMapper[writableFields.length];
            for (int i = 0; i < writableFields.length; i = i + 1) {
                writableFieldNodeMappers[i] = context.findMapper(writableFields[i].fieldType());
            }
        }

        // 获取配置
        var options = context.getMapperOptions(BeanNodeMapperOptions.class, BEAN_NODE_MAPPER_OPTIONS);
        var readPolicy = options.beanFieldReadPolicy();

        // 转换
        var object = newInstance();

        for (int i = 0; i < writableFields.length; i = i + 1) {
            var fieldInfo = writableFields[i];

            var name = fieldInfo.name();

            if (readPolicy != null) {
                var result = readPolicy.apply(fieldInfo);
                if (result != null) {
                    // 如果需要跳过
                    if (result.skip()) {
                        continue;
                    }
                    name = result.name();
                }
            }

            var tempNode = node.get(name);
            // 这里不要把 null 传递到 nodeToObject 中防止引发错误
            if (tempNode == null) {
                tempNode = NULL;
            }
            var v = context.nodeToObject(tempNode, writableFieldNodeMappers[i]);
            setFieldValue(fieldInfo, object, v);
        }

        return object;
    }

    private Object newInstance() throws NodeToObjectException {
        if (classInfo.isAbstract()) {
            throw new NodeToObjectException("Abstract class 无法被实例化: " + classInfo);
        }
        if (defaultConstructor == null) {
            throw new NodeToObjectException("未找到可用的无参构造函数: " + classInfo);
        }
        try {
            return defaultConstructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // 这里可能因为构造函数内部异常, 或反射访问仍然不可用而失败.
            throw new NodeToObjectException(e);
        }
    }

}
