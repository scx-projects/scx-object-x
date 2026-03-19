package dev.scx.object.x.mapper.record;

import dev.scx.node.ObjectNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.*;

import java.lang.reflect.InvocationTargetException;

import static dev.scx.node.NullNode.NULL;

/// RecordNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class RecordNodeMapper implements TypeNodeMapper<Object, ObjectNode> {

    private static final RecordNodeMapperOptions RECORD_NODE_MAPPER_OPTIONS = new RecordNodeMapperOptions();

    private final ClassInfo classInfo;
    private final RecordComponentInfo[] recordComponents;
    private final ConstructorInfo recordConstructor;
    private final ParameterInfo[] parameters;
    // 性能优化: 缓存 parameters 对应的 TypeNodeMapper, 避免在每个 ParameterInfo 转换时重复执行 findMapper.
    private TypeNodeMapper<?, ?>[] parameterNodeMappers;

    public RecordNodeMapper(ClassInfo classInfo) {
        this.classInfo = classInfo;
        // 因为 ClassInfo.recordComponents() 内部每次都会 clone 一次, 这里为了性能单独存一份, 其余同理.
        this.recordComponents = this.classInfo.recordComponents();
        this.recordConstructor = this.classInfo.recordConstructor();
        this.parameters = this.recordConstructor.parameters();
        this.parameterNodeMappers = null;
    }

    private static Object getComponentValue(RecordComponentInfo componentInfo, Object value) throws ObjectToNodeException {
        try {
            return componentInfo.get(value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ObjectToNodeException(e);
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
    public ObjectNode valueToNode(Object recordValue, ObjectToNodeContext context) throws ObjectToNodeException {
        var options = context.getMapperOptions(RecordNodeMapperOptions.class, RECORD_NODE_MAPPER_OPTIONS);
        var writePolicy = options.recordComponentWritePolicy();

        var objectNode = new ObjectNode();

        for (var recordComponent : recordComponents) {
            var name = recordComponent.name();
            var value = getComponentValue(recordComponent, recordValue);

            if (writePolicy != null) {
                var result = writePolicy.apply(recordComponent, value);
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
        if (parameterNodeMappers == null) {
            parameterNodeMappers = new TypeNodeMapper[parameters.length];
            for (int i = 0; i < parameters.length; i = i + 1) {
                parameterNodeMappers[i] = context.findMapper(parameters[i].parameterType());
            }
        }

        // 获取配置
        var options = context.getMapperOptions(RecordNodeMapperOptions.class, RECORD_NODE_MAPPER_OPTIONS);
        var readPolicy = options.recordParameterReadPolicy();

        // 转换
        var params = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i = i + 1) {
            var parameter = parameters[i];
            var name = parameter.name();

            if (readPolicy != null) {
                var result = readPolicy.apply(parameter);
                if (result != null) {
                    // 如果需要跳过
                    if (result.skip()) {
                        // 和 bean 不同, 这里不能直接 continue, 需要保证的 params[i] 有值, 但永远从 NULL 映射而来.
                        params[i] = context.nodeToObject(NULL, parameterNodeMappers[i]);
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
            params[i] = context.nodeToObject(tempNode, parameterNodeMappers[i]);
        }

        return newInstance(params);
    }

    private Object newInstance(Object... params) throws NodeToObjectException {
        try {
            return recordConstructor.newInstance(params);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // 因为我们使用的都是 public 构造函数, 所以这里理论上不会出现异常, 除非构造函数方法内部逻辑异常
            throw new NodeToObjectException(e);
        }
    }

}
