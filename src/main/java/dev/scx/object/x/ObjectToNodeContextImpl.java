package dev.scx.object.x;

import dev.scx.node.Node;
import dev.scx.node.NullNode;
import dev.scx.object.ObjectToNodeException;
import dev.scx.reflect.TypeInfo;

import java.util.Map;

/// Object -> Node 上下文
///
/// 一般来说 这里应该支持递归引用检测,
/// 但经过实际测试有些耗费性能, 这里暂时只提供 最大嵌套层数检测
///
/// @author scx567888
/// @version 0.0.1
public final class ObjectToNodeContextImpl implements ObjectToNodeContext {

    /// NodeMapper 选择器
    private final TypeNodeMapperSelector selector;
    /// 最大深度
    private final int maxNestingDepth;
    /// mapperOptionsMap
    private final Map<Class<? extends TypeNodeMapperOptions>, TypeNodeMapperOptions> mapperOptionsMap;
    /// 当前深度
    private int nestingDepth;

    public ObjectToNodeContextImpl(TypeNodeMapperSelector selector, DefaultObjectNodeConvertOptions options) {
        this.selector = selector;
        this.maxNestingDepth = options.maxNestingDepth();
        this.mapperOptionsMap = options.mapperOptionsMap();
        this.nestingDepth = 0;
    }

    @Override
    public Node objectToNode(Object value, Object pathSegment) throws ObjectToNodeException {
        if (value == null) {
            return NullNode.NULL;
        }
        var mapper = findMapper(value.getClass());
        return objectToNode0(value, pathSegment, mapper);
    }

    @Override
    public Node objectToNode(Object value, Object pathSegment, TypeNodeMapper<?, ?> mapper) throws ObjectToNodeException {
        if (value == null) {
            return NullNode.NULL;
        }
        return objectToNode0(value, pathSegment, mapper);
    }

    @Override
    public TypeNodeMapper<?, ?> findMapper(TypeInfo type) throws ObjectToNodeException {
        var mapper = selector.findMapper(type);
        if (mapper == null) {
            throw new ObjectToNodeException("No TypeNodeMapper found for type " + type);
        }
        return mapper;
    }

    @Override
    public TypeNodeMapper<?, ?> findMapper(Class<?> type) throws ObjectToNodeException {
        var mapper = selector.findMapper(type);
        if (mapper == null) {
            throw new ObjectToNodeException("No TypeNodeMapper found for type " + type);
        }
        return mapper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends TypeNodeMapperOptions> T getMapperOptions(Class<T> optionsType) {
        if (mapperOptionsMap == null) {
            return null;
        }
        return (T) mapperOptionsMap.get(optionsType);
    }

    @SuppressWarnings("unchecked")
    private Node objectToNode0(Object value, Object pathSegment, TypeNodeMapper<?, ?> mapper) throws ObjectToNodeException {
        // 判断嵌套深度
        if (nestingDepth > maxNestingDepth) {
            throw new ObjectToNodeException("嵌套深度超过限制: 最大 " + maxNestingDepth);
        }
        nestingDepth = nestingDepth + 1;
        try {
            var m = (TypeNodeMapper<Object, Node>) mapper;
            return m.valueToNode(value, this);
        } finally {
            // 回退嵌套深度
            nestingDepth = nestingDepth - 1;
        }
    }

}
