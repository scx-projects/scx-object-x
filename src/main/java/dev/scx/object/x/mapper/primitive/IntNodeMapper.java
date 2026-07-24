package dev.scx.object.x.mapper.primitive;

import dev.scx.node.IntNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.NumberConversionPolicy;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// IntNodeMapper
///
/// @author scx567888
public final class IntNodeMapper implements TypeNodeMapper<Integer, ValueNode> {

    private final boolean isPrimitive;

    public IntNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return isPrimitive ? typeOf(int.class) : typeOf(Integer.class);
    }

    @Override
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Integer value, ObjectToNodeContext context) {
        return new IntNode(value);
    }

    @Override
    public Integer nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            var conversionPolicy = context.getMapperOptions(NumberConversionPolicy.class, NumberConversionPolicy.DEFAULT);
            return switch (conversionPolicy) {
                case DEFAULT -> node.asInt();
                case EXACT -> node.asIntExact();
            };
        } catch (NumberFormatException | ArithmeticException e) {
            throw new NodeToObjectException(e);
        }
    }

    @Override
    public Integer nullNodeToValue(NodeToObjectContext context) throws NodeToObjectException {
        if (isPrimitive) {
            var nullPolicy = context.getMapperOptions(PrimitiveNullPolicy.class, PrimitiveNullPolicy.ERROR);
            return switch (nullPolicy) {
                case ERROR -> throw new NodeToObjectException("can not convert NullNode to primitive int");
                case DEFAULT_VALUE -> 0;
            };
        }
        return null;
    }

}
