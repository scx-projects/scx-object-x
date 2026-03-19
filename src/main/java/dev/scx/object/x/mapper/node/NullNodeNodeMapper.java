package dev.scx.object.x.mapper.node;

import dev.scx.node.NullNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// NullNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
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

}
