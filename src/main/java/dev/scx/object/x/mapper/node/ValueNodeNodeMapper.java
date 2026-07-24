package dev.scx.object.x.mapper.node;

import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ValueNodeNodeMapper
///
/// @author scx567888
public final class ValueNodeNodeMapper implements TypeNodeMapper<ValueNode, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(ValueNode.class);
    }

    @Override
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(ValueNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public ValueNode nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        return node.deepCopy();
    }

}
