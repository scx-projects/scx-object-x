package dev.scx.object.x.mapper.math;

import dev.scx.node.BigIntegerNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.math.BigInteger;

import static dev.scx.reflect.ScxReflect.typeOf;

/// BigIntegerNodeMapper
///
/// @author scx567888
/// @version 0.0.1
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
            return node.asBigInteger();
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

}
