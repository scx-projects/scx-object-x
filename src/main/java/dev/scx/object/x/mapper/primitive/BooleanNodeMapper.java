package dev.scx.object.x.mapper.primitive;

import dev.scx.node.BooleanNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.object.x.mapper.primitive.PrimitiveNullPolicy.ERROR;
import static dev.scx.reflect.ScxReflect.typeOf;

/// BooleanNodeMapper
///
/// @author scx567888
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
    public Boolean nullNodeToValue(NodeToObjectContext context) throws NodeToObjectException {
        if (isPrimitive) {
            var nullPolicy = context.getMapperOptions(PrimitiveNullPolicy.class, ERROR);
            return switch (nullPolicy) {
                case ERROR -> throw new NodeToObjectException("can not convert NullNode to primitive boolean");
                case DEFAULT_VALUE -> false;
            };
        }
        return null;
    }

}
