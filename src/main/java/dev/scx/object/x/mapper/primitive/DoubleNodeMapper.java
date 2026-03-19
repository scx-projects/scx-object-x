package dev.scx.object.x.mapper.primitive;

import dev.scx.node.DoubleNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// DoubleNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class DoubleNodeMapper implements TypeNodeMapper<Double, ValueNode> {

    private final boolean isPrimitive;

    public DoubleNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return isPrimitive ? typeOf(double.class) : typeOf(Double.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Double value, ObjectToNodeContext context) {
        return new DoubleNode(value);
    }

    @Override
    public Double nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return node.asDouble();
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

    @Override
    public Double nullNodeToValue() throws NodeToObjectException {
        return isPrimitive ? 0.0 : null;
    }

}
