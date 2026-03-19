package dev.scx.object.x.mapper.math;

import dev.scx.node.BigDecimalNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.math.BigDecimal;

import static dev.scx.reflect.ScxReflect.typeOf;

/// BigDecimalNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class BigDecimalNodeMapper implements TypeNodeMapper<BigDecimal, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(BigDecimal.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(BigDecimal value, ObjectToNodeContext context) {
        return new BigDecimalNode(value);
    }

    @Override
    public BigDecimal nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return node.asBigDecimal();
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

}
