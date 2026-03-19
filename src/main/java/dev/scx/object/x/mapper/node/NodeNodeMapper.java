package dev.scx.object.x.mapper.node;

import dev.scx.node.Node;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// NodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class NodeNodeMapper implements TypeNodeMapper<Node, Node> {

    @Override
    public TypeInfo valueType() {
        return typeOf(Node.class);
    }

    @Override
    public Class<Node> nodeType() {
        return Node.class;
    }

    @Override
    public Node valueToNode(Node value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public Node nodeToValue(Node node, NodeToObjectContext context) {
        return node.deepCopy();
    }

}
