package dev.scx.object.x.mapper.primitive;

import dev.scx.node.DoubleNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.NumberConversionPolicy;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// DoubleNodeMapper
///
/// @author scx567888
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
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Double value, ObjectToNodeContext context) {
        return new DoubleNode(value);
    }

    @Override
    public Double nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            var conversionPolicy = context.getMapperOptions(NumberConversionPolicy.class, NumberConversionPolicy.DEFAULT);
            return switch (conversionPolicy) {
                case DEFAULT -> node.asDouble();
                case EXACT -> node.asDoubleExact();
            };
        } catch (NumberFormatException | ArithmeticException e) {
            throw new NodeToObjectException(e);
        }
    }

    @Override
    public Double nullNodeToValue(NodeToObjectContext context) throws NodeToObjectException {
        if (isPrimitive) {
            var nullPolicy = context.getMapperOptions(PrimitiveNullPolicy.class, PrimitiveNullPolicy.ERROR);
            return switch (nullPolicy) {
                case ERROR -> throw new NodeToObjectException("can not convert NullNode to primitive double");
                case DEFAULT_VALUE -> 0.0;
            };
        }
        return null;
    }

}
