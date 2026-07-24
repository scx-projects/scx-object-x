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

/// ByteNodeMapper
///
/// @author scx567888
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
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Byte value, ObjectToNodeContext context) {
        return new IntNode(value);
    }

    @Override
    public Byte nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            var conversionPolicy = context.getMapperOptions(NumberConversionPolicy.class, NumberConversionPolicy.DEFAULT);
            return switch (conversionPolicy) {
                case DEFAULT -> (byte) node.asInt();
                case EXACT -> {
                    var value = node.asIntExact();
                    if ((int) (byte) value != value) {
                        throw new ArithmeticException("Precision loss: " + value);
                    }
                    yield (byte) value;
                }
            };
        } catch (NumberFormatException | ArithmeticException e) {
            throw new NodeToObjectException(e);
        }
    }

    @Override
    public Byte nullNodeToValue(NodeToObjectContext context) throws NodeToObjectException {
        if (isPrimitive) {
            var nullPolicy = context.getMapperOptions(PrimitiveNullPolicy.class, PrimitiveNullPolicy.ERROR);
            return switch (nullPolicy) {
                case ERROR -> throw new NodeToObjectException("can not convert NullNode to primitive byte");
                case DEFAULT_VALUE -> (byte) 0;
            };
        }
        return null;
    }

}
