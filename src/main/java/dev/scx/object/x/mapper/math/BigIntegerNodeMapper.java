package dev.scx.object.x.mapper.math;

import dev.scx.node.BigIntegerNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.NumberConversionPolicy;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.math.BigInteger;

import static dev.scx.reflect.ScxReflect.typeOf;

/// BigIntegerNodeMapper
///
/// @author scx567888
public final class BigIntegerNodeMapper implements TypeNodeMapper<BigInteger, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(BigInteger.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(BigInteger value, ObjectToNodeContext context) {
        return new BigIntegerNode(value);
    }

    @Override
    public BigInteger nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            var conversionPolicy = context.getMapperOptions(NumberConversionPolicy.class, NumberConversionPolicy.DEFAULT);
            return switch (conversionPolicy) {
                case DEFAULT -> node.asBigInteger();
                case EXACT -> node.asBigIntegerExact();
            };
        } catch (NumberFormatException | ArithmeticException e) {
            throw new NodeToObjectException(e);
        }
    }

}
