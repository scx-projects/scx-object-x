package dev.scx.object.x.mapper.primitive;

import dev.scx.node.FloatNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// FloatNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class FloatNodeMapper implements TypeNodeMapper<Float, ValueNode> {

    private final boolean isPrimitive;

    public FloatNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return isPrimitive ? typeOf(float.class) : typeOf(Float.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Float value, ObjectToNodeContext context) {
        return new FloatNode(value);
    }

    @Override
    public Float nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return node.asFloat();
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

    @Override
    public Float nullNodeToValue() throws NodeToObjectException {
        return isPrimitive ? 0f : null;
    }

}
