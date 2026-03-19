package dev.scx.object.x.mapper.node;

import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ValueNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class ValueNodeNodeMapper implements TypeNodeMapper<ValueNode, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(ValueNode.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
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
