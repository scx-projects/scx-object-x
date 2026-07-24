package dev.scx.object.x.mapper.primitive;

import dev.scx.node.LongNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.NumberConversionPolicy;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// LongNodeMapper
///
/// @author scx567888
public final class LongNodeMapper implements TypeNodeMapper<Long, ValueNode> {

    private final boolean isPrimitive;

    public LongNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return isPrimitive ? typeOf(long.class) : typeOf(Long.class);
    }

    @Override
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Long value, ObjectToNodeContext context) {
        return new LongNode(value);
    }

    @Override
    public Long nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            var conversionPolicy = context.getMapperOptions(NumberConversionPolicy.class, NumberConversionPolicy.DEFAULT);
            return switch (conversionPolicy) {
                case DEFAULT -> node.asLong();
                case EXACT -> node.asLongExact();
            };
        } catch (NumberFormatException | ArithmeticException e) {
            throw new NodeToObjectException(e);
        }
    }

    @Override
    public Long nullNodeToValue(NodeToObjectContext context) throws NodeToObjectException {
        if (isPrimitive) {
            var nullPolicy = context.getMapperOptions(PrimitiveNullPolicy.class, PrimitiveNullPolicy.ERROR);
            return switch (nullPolicy) {
                case ERROR -> throw new NodeToObjectException("can not convert NullNode to primitive long");
                case DEFAULT_VALUE -> 0L;
            };
        }
        return null;
    }

}
