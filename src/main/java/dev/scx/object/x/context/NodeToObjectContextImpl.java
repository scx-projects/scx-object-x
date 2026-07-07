package dev.scx.object.x.context;

import dev.scx.node.Node;
import dev.scx.node.NullNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.DefaultObjectNodeConvertOptions;
import dev.scx.object.x.adapter.NodeTypeAdapter;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.TypeNodeMapperOptions;
import dev.scx.object.x.mapper.TypeNodeMapperSelector;
import dev.scx.reflect.TypeInfo;

import java.util.Map;

/// Node -> Object 上下文
///
/// 一般来说 这里应该支持递归引用检测 (是的 Node 也是可能递归引用的),
/// 但经过实际测试有些耗费性能, 这里暂时只提供 最大嵌套层数检测
///
/// @author scx567888
public final class NodeToObjectContextImpl implements NodeToObjectContext {

    /// NodeMapper 选择器
    private final TypeNodeMapperSelector selector;
    /// 最大深度
    private final int maxNestingDepth;
    /// mapperOptionsMap
    private final Map<Class<? extends TypeNodeMapperOptions>, TypeNodeMapperOptions> mapperOptionsMap;
    /// node 类型适配器
    private final NodeTypeAdapter nodeTypeAdapter;
    /// 当前深度
    private int nestingDepth;

    public NodeToObjectContextImpl(TypeNodeMapperSelector selector, DefaultObjectNodeConvertOptions options) {
        this.selector = selector;
        this.maxNestingDepth = options.maxNestingDepth();
        this.mapperOptionsMap = options.mapperOptionsMap();
        this.nodeTypeAdapter = options.nodeTypeAdapter();
        this.nestingDepth = 0;
    }

    @Override
    public <T> T nodeToObject(Node node, TypeInfo type) throws NodeToObjectException {
        if (node == null) {
            throw new NodeToObjectException("node must not be null");
        }
        var mapper = findMapper(type);
        return nodeToObject0(node, mapper, this.nodeTypeAdapter, node);
    }

    @Override
    public <T> T nodeToObject(Node node, Class<T> clazz) throws NodeToObjectException {
        if (node == null) {
            throw new NodeToObjectException("node must not be null");
        }
        var mapper = findMapper(clazz);
        return nodeToObject0(node, mapper, this.nodeTypeAdapter, node);
    }

    @Override
    public <T> T nodeToObject(Node node, TypeNodeMapper<?, ?> mapper) {
        if (node == null) {
            throw new NodeToObjectException("node must not be null");
        }
        return nodeToObject0(node, mapper, this.nodeTypeAdapter, node);
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
    private <T> T nodeToObject0(Node node, TypeNodeMapper<?, ?> mapper, NodeTypeAdapter nodeTypeAdapter, Node originalNode) {
        // 判断嵌套深度
        if (nestingDepth > maxNestingDepth) {
            throw new NodeToObjectException("嵌套深度超过限制: 最大 " + maxNestingDepth);
        }
        nestingDepth = nestingDepth + 1;
        try {

            // 1, 先处理 NullNode.NULL
            if (node == NullNode.NULL) {
                return (T) mapper.nullNodeToValue(this);
            }

            var expectedNodeType = mapper.nodeType();

            // 2. 匹配成功直接转换
            if (expectedNodeType.isInstance(node)) {
                var m = (TypeNodeMapper<?, Node>) mapper;
                return (T) m.nodeToValue(node, this);
            }

            // 3. 如果 Node 类型不匹配, 我们尝试 使用 NodeTypeAdapter 适配. 然后重新递归转换.
            if (nodeTypeAdapter != null) {
                var adaptedNode = nodeTypeAdapter.adapt(node, expectedNodeType);
                if (adaptedNode != null) {
                    // 递归时将 nodeTypeAdapter 置空 因为我们只尝试适配一次.
                    return nodeToObject0(adaptedNode, mapper, null, originalNode);
                }
            }

            // 4, 抛异常
            throw new NodeToObjectException("Node type mismatch, expected: " + expectedNodeType.getName() + ", got: " + originalNode.getClass().getName());

        } finally {
            // 回退嵌套深度
            nestingDepth = nestingDepth - 1;
        }
    }

}
