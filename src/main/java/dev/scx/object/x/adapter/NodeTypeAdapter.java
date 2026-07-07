package dev.scx.object.x.adapter;

import dev.scx.node.Node;

/// NodeTypeAdapter
///
/// @author scx567888
public interface NodeTypeAdapter {

    /// 无法处理返回 null.
    Node adapt(Node node, Class<? extends Node> expectedNodeType);

}
