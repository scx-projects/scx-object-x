package dev.scx.object.x.mapper.node;

import dev.scx.node.NullNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.node.NullNode.NULL;
import static dev.scx.reflect.ScxReflect.typeOf;

/// NullNodeNodeMapper
///
/// @author scx567888
public final class NullNodeNodeMapper implements TypeNodeMapper<NullNode, NullNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(NullNode.class);
    }

    @Override
    public Class<NullNode> nodeType() {
        return NullNode.class;
    }

    @Override
    public NullNode valueToNode(NullNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public NullNode nodeToValue(NullNode node, NodeToObjectContext context) throws NodeToObjectException {
        return node.deepCopy();
    }

    @Override
    public NullNode nullNodeToValue(NodeToObjectContext context) throws NodeToObjectException {
        return NULL;
    }

}
