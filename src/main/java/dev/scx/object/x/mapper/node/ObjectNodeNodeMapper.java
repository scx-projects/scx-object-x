package dev.scx.object.x.mapper.node;

import dev.scx.node.ObjectNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ObjectNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class ObjectNodeNodeMapper implements TypeNodeMapper<ObjectNode, ObjectNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(ObjectNode.class);
    }

    @Override
    public Class<ObjectNode> nodeType() {
        return ObjectNode.class;
    }

    @Override
    public ObjectNode valueToNode(ObjectNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public ObjectNode nodeToValue(ObjectNode node, NodeToObjectContext context) throws NodeToObjectException {
        return node.deepCopy();
    }

}
