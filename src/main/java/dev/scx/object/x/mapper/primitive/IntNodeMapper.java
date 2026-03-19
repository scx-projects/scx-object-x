package dev.scx.object.x.mapper.primitive;

import dev.scx.node.IntNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// IntNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class IntNodeMapper implements TypeNodeMapper<Integer, ValueNode> {

    private final boolean isPrimitive;

    public IntNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return isPrimitive ? typeOf(int.class) : typeOf(Integer.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Integer value, ObjectToNodeContext context) {
        return new IntNode(value);
    }

    @Override
    public Integer nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return node.asInt();
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

    @Override
    public Integer nullNodeToValue() throws NodeToObjectException {
        return isPrimitive ? 0 : null;
    }

}
