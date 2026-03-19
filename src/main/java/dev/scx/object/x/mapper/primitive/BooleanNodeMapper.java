package dev.scx.object.x.mapper.primitive;

import dev.scx.node.BooleanNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// BooleanNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class BooleanNodeMapper implements TypeNodeMapper<Boolean, ValueNode> {

    private final boolean isPrimitive;

    public BooleanNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return isPrimitive ? typeOf(boolean.class) : typeOf(Boolean.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Boolean value, ObjectToNodeContext context) {
        return BooleanNode.of(value);
    }

    @Override
    public Boolean nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        return node.asBoolean();
    }

    @Override
    public Boolean nullNodeToValue() throws NodeToObjectException {
        return isPrimitive ? false : null;
    }

}
