package dev.scx.object.x.mapper.atomic;

import dev.scx.node.IntNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.NumberConversionPolicy;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.util.concurrent.atomic.AtomicInteger;

import static dev.scx.reflect.ScxReflect.typeOf;

/// AtomicIntegerNodeMapper
///
/// @author scx567888
public final class AtomicIntegerNodeMapper implements TypeNodeMapper<AtomicInteger, ValueNode> {

    public AtomicIntegerNodeMapper() {

    }

    @Override
    public TypeInfo valueType() {
        return typeOf(AtomicInteger.class);
    }

    @Override
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(AtomicInteger value, ObjectToNodeContext context) {
        return new IntNode(value.get());
    }

    @Override
    public AtomicInteger nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            var conversionPolicy = context.getMapperOptions(NumberConversionPolicy.class, NumberConversionPolicy.DEFAULT);
            var value= switch (conversionPolicy) {
                case DEFAULT -> node.asInt();
                case EXACT -> node.asIntExact();
            };
            return new AtomicInteger(value);
        } catch (NumberFormatException | ArithmeticException e) {
            throw new NodeToObjectException(e);
        }
    }

}
