package dev.scx.object.x.adapter;

import dev.scx.node.ArrayNode;
import dev.scx.node.Node;

/// SingleElementArrayUnwrapAdapter
///
/// @author scx567888
public final class SingleElementArrayUnwrapAdapter implements NodeTypeAdapter {

    public static final SingleElementArrayUnwrapAdapter SINGLE_ELEMENT_ARRAY_UNWRAP_ADAPTER = new SingleElementArrayUnwrapAdapter();

    /// 私有构造函数, 保证单例
    private SingleElementArrayUnwrapAdapter() {

    }

    @Override
    public Node adapt(Node node, Class<? extends Node> expectedNodeType) {
        // 这里只需要判断 node, 无需判断 expectedNodeType 的类型.
        // 因为进入 adapt 时, expectedNodeType 必然不是 ArrayNode.class.
        if (node instanceof ArrayNode arrayNode && arrayNode.size() == 1) {
            return arrayNode.get(0);
        }
        return null;
    }

}
