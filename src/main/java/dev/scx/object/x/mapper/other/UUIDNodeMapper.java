package dev.scx.object.x.mapper.other;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.util.UUID;

import static dev.scx.reflect.ScxReflect.typeOf;

/// UUIDNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class UUIDNodeMapper implements TypeNodeMapper<UUID, StringNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(UUID.class);
    }

    @Override
    public Class<StringNode> nodeType() {
        return StringNode.class;
    }

    @Override
    public StringNode valueToNode(UUID value, ObjectToNodeContext context) throws ObjectToNodeException {
        return new StringNode(value.toString());
    }

    @Override
    public UUID nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        var value = node.value();
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new NodeToObjectException(e);
        }
    }

}
