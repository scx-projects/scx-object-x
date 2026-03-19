package dev.scx.object.x.mapper.primitive;

import dev.scx.node.IntNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ByteNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class ByteNodeMapper implements TypeNodeMapper<Byte, ValueNode> {

    private final boolean isPrimitive;

    public ByteNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return isPrimitive ? typeOf(byte.class) : typeOf(Byte.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Byte value, ObjectToNodeContext context) {
        return new IntNode(value);
    }

    @Override
    public Byte nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return (byte) node.asInt();
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

    @Override
    public Byte nullNodeToValue() throws NodeToObjectException {
        return isPrimitive ? (byte) 0 : null;
    }

}
