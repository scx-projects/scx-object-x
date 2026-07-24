package dev.scx.object.x.mapper.node;

import dev.scx.node.ArrayNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ArrayNodeNodeMapper
///
/// @author scx567888
public final class ArrayNodeNodeMapper implements TypeNodeMapper<ArrayNode, ArrayNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(ArrayNode.class);
    }

    @Override
    public Class<ArrayNode> nodeType(NodeToObjectContext context) {
        return ArrayNode.class;
    }

    @Override
    public ArrayNode valueToNode(ArrayNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public ArrayNode nodeToValue(ArrayNode node, NodeToObjectContext context) throws NodeToObjectException {
        return node.deepCopy();
    }

}
