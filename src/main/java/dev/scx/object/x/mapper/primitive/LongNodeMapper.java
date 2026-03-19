package dev.scx.object.x.mapper.primitive;

import dev.scx.node.LongNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// LongNodeMapper
///
/// @author scx567888
/// @version 0.0.1
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
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Long value, ObjectToNodeContext context) {
        return new LongNode(value);
    }

    @Override
    public Long nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return node.asLong();
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

    @Override
    public Long nullNodeToValue() throws NodeToObjectException {
        return isPrimitive ? 0L : null;
    }

}
