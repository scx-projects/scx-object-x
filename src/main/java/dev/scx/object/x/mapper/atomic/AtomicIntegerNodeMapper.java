package dev.scx.object.x.mapper.atomic;

import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.primitive.IntNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.util.concurrent.atomic.AtomicInteger;

import static dev.scx.reflect.ScxReflect.typeOf;

/// AtomicIntegerNodeMapper
///
/// @author scx567888
public final class AtomicIntegerNodeMapper implements TypeNodeMapper<AtomicInteger, ValueNode> {

    private final IntNodeMapper intNodeMapper;

    public AtomicIntegerNodeMapper() {
        this.intNodeMapper = new IntNodeMapper(true);
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(AtomicInteger.class);
    }

    @Override
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
        return intNodeMapper.nodeType(context);
    }

    @Override
    public ValueNode valueToNode(AtomicInteger value, ObjectToNodeContext context) {
        return intNodeMapper.valueToNode(value.get(), context);
    }

    @Override
    public AtomicInteger nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        var value = intNodeMapper.nodeToValue(node, context);
        return new AtomicInteger(value);
    }

}
