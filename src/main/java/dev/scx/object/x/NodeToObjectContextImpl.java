package dev.scx.object.x;

import dev.scx.node.Node;
import dev.scx.node.NullNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.reflect.TypeInfo;

import java.util.Map;

/// Node -> Object 上下文
///
/// 一般来说 这里应该支持递归引用检测 (是的 Node 也是可能递归引用的),
/// 但经过实际测试有些耗费性能, 这里暂时只提供 最大嵌套层数检测
///
/// @author scx567888
/// @version 0.0.1
public final class NodeToObjectContextImpl implements NodeToObjectContext {

    /// NodeMapper 选择器
    private final TypeNodeMapperSelector selector;
    /// 最大深度
    private final int maxNestingDepth;
    /// mapperOptionsMap
    private final Map<Class<? extends TypeNodeMapperOptions>, TypeNodeMapperOptions> mapperOptionsMap;
    /// 当前深度
    private int nestingDepth;

    public NodeToObjectContextImpl(TypeNodeMapperSelector selector, DefaultObjectNodeConvertOptions options) {
        this.selector = selector;
        this.maxNestingDepth = options.maxNestingDepth();
        this.mapperOptionsMap = options.mapperOptionsMap();
        this.nestingDepth = 0;
    }

    @Override
    public <T> T nodeToObject(Node node, TypeInfo type) throws NodeToObjectException {
        if (node == null) {
            throw new NodeToObjectException("node must not be null");
        }
        var mapper = findMapper(type);
        return nodeToObject0(node, mapper);
    }

    @Override
    public <T> T nodeToObject(Node node, Class<T> clazz) throws NodeToObjectException {
        if (node == null) {
            throw new NodeToObjectException("node must not be null");
        }
        var mapper = findMapper(clazz);
        return nodeToObject0(node, mapper);
    }

    @Override
    public <T> T nodeToObject(Node node, TypeNodeMapper<?, ?> mapper) {
        if (node == null) {
            throw new NodeToObjectException("node must not be null");
        }
        return nodeToObject0(node, mapper);
    }

    @Override
    public TypeNodeMapper<?, ?> findMapper(TypeInfo type) {
        var mapper = selector.findMapper(type);
        if (mapper == null) {
            throw new NodeToObjectException("No NodeMapper found for type " + type);
        }
        return mapper;
    }

    @Override
    public TypeNodeMapper<?, ?> findMapper(Class<?> type) {
        var mapper = selector.findMapper(type);
        if (mapper == null) {
            throw new NodeToObjectException("No NodeMapper found for type " + type);
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
    private <T> T nodeToObject0(Node node, TypeNodeMapper<?, ?> mapper) {
        // 判断嵌套深度
        if (nestingDepth > maxNestingDepth) {
            throw new NodeToObjectException("嵌套深度超过限制: 最大 " + maxNestingDepth);
        }
        nestingDepth = nestingDepth + 1;
        try {

            // 1, 先处理 NullNode.NULL
            if (node == NullNode.NULL) {
                return (T) mapper.nullNodeToValue();
            }

            // 2. 匹配成功直接转换
            if (mapper.nodeType().isInstance(node)) {
                var m = (TypeNodeMapper<?, Node>) mapper;
                return (T) m.nodeToValue(node, this);
            }

            // 3, 抛异常
            throw new NodeToObjectException("Node type mismatch, expected: " + mapper.nodeType().getName() + ", got: " + node.getClass().getName());

        } finally {
            // 回退嵌套深度
            nestingDepth = nestingDepth - 1;
        }
    }

}
