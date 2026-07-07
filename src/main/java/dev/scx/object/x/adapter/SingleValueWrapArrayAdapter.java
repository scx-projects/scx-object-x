package dev.scx.object.x.adapter;

import dev.scx.node.ArrayNode;
import dev.scx.node.Node;

/// SingleValueWrapArrayAdapter
///
/// @author scx567888
public final class SingleValueWrapArrayAdapter implements NodeTypeAdapter {

    public static final SingleValueWrapArrayAdapter SINGLE_VALUE_WRAP_ARRAY_ADAPTER = new SingleValueWrapArrayAdapter();

    /// 私有构造函数, 保证单例
    private SingleValueWrapArrayAdapter() {

    }

    @Override
    public Node adapt(Node node, Class<? extends Node> expectedNodeType) {
        // 这里只需要判断 expectedNodeType, 无需判断 node 的类型.
        // 因为进入 adapt 时, node 必然不是 ArrayNode.
        if (expectedNodeType == ArrayNode.class) {
            var arrayNode = new ArrayNode();
            arrayNode.add(node);
            return arrayNode;
        }
        return null;
    }

}
