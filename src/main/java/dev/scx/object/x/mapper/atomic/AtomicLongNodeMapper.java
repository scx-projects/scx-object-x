package dev.scx.object.x.mapper.atomic;

import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.primitive.LongNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.util.concurrent.atomic.AtomicLong;

import static dev.scx.reflect.ScxReflect.typeOf;

/// AtomicLongNodeMapper
///
/// @author scx567888
public final class AtomicLongNodeMapper implements TypeNodeMapper<AtomicLong, ValueNode> {

    private final LongNodeMapper longNodeMapper;

    public AtomicLongNodeMapper() {
        this.longNodeMapper = new LongNodeMapper(true);
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(AtomicLong.class);
    }

    @Override
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
        return longNodeMapper.nodeType(context);
    }

    @Override
    public ValueNode valueToNode(AtomicLong value, ObjectToNodeContext context) {
        return longNodeMapper.valueToNode(value.get(), context);
    }

    @Override
    public AtomicLong nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        var value = longNodeMapper.nodeToValue(node, context);
        return new AtomicLong(value);
    }

}
