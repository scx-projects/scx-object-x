package dev.scx.object.x.mapper.primitive;

import dev.scx.node.IntNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ShortNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class ShortNodeMapper implements TypeNodeMapper<Short, ValueNode> {

    private final boolean isPrimitive;

    public ShortNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return isPrimitive ? typeOf(short.class) : typeOf(Short.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Short value, ObjectToNodeContext context) {
        return new IntNode(value);
    }

    @Override
    public Short nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return (short) node.asInt();
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

    @Override
    public Short nullNodeToValue() throws NodeToObjectException {
        return isPrimitive ? (short) 0 : null;
    }

}
