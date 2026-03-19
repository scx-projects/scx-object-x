package dev.scx.object.x.mapper.bean;

import dev.scx.node.ObjectNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.*;

import java.lang.reflect.InvocationTargetException;

import static dev.scx.node.NullNode.NULL;

/// 通用对象处理器
///
/// @author scx567888
/// @version 0.0.1
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

    public BeanNodeMapper(ClassInfo classInfo, ConstructorInfo defaultConstructor, FieldInfo[] readableFields, FieldInfo[] writableFields) {
        this.classInfo = classInfo;
        this.defaultConstructor = defaultConstructor;
        this.readableFields = readableFields;
        this.writableFields = writableFields;
        this.writableFieldNodeMappers = null;
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
        if (defaultConstructor == null) {
            throw new NodeToObjectException("未找到可用的构造函数: " + classInfo);
        }
        if (defaultConstructor.accessModifier() != AccessModifier.PUBLIC) {
            throw new NodeToObjectException("未找到可用的 public 构造函数: " + classInfo);
        }
        try {
            return defaultConstructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // 因为我们使用的都是 public 构造函数, 所以这里理论上不会出现异常, 除非构造函数方法内部逻辑异常
            throw new NodeToObjectException(e);
        }
    }

}
