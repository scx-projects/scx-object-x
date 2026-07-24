package dev.scx.object.x.mapper.atomic;

import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.primitive.BooleanNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.util.concurrent.atomic.AtomicBoolean;

import static dev.scx.reflect.ScxReflect.typeOf;

/// AtomicBooleanNodeMapper
///
/// @author scx567888
public final class AtomicBooleanNodeMapper implements TypeNodeMapper<AtomicBoolean, ValueNode> {

    private final BooleanNodeMapper booleanNodeMapper;

    public AtomicBooleanNodeMapper() {
        this.booleanNodeMapper = new BooleanNodeMapper(true);
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(AtomicBoolean.class);
    }

    @Override
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
        return booleanNodeMapper.nodeType(context);
    }

    @Override
    public ValueNode valueToNode(AtomicBoolean value, ObjectToNodeContext context) {
        return booleanNodeMapper.valueToNode(value.get(), context);
    }

    @Override
    public AtomicBoolean nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        var value = booleanNodeMapper.nodeToValue(node, context);
        return new AtomicBoolean(value);
    }

}
