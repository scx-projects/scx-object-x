package dev.scx.object.x.adapter;

import dev.scx.node.Node;

import java.util.List;

/// CompositeNodeTypeAdapter
///
/// @author scx567888
public final class CompositeNodeTypeAdapter implements NodeTypeAdapter {

    private final List<NodeTypeAdapter> nodeTypeAdapters;

    public CompositeNodeTypeAdapter(NodeTypeAdapter... nodeTypeAdapters) {
        this.nodeTypeAdapters = List.of(nodeTypeAdapters);
    }

    @Override
    public Node adapt(Node node, Class<? extends Node> expectedNodeType) {
        for (var nodeTypeAdapter : nodeTypeAdapters) {
            var adapted = nodeTypeAdapter.adapt(node, expectedNodeType);
            if (adapted != null) {
                return adapted;
            }
        }
        return null;
    }

}
