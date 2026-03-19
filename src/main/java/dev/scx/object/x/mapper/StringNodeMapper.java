package dev.scx.object.x.mapper;

import dev.scx.node.StringNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// StringNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class StringNodeMapper implements TypeNodeMapper<String, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(String.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(String value, ObjectToNodeContext context) {
        return new StringNode(value);
    }

    @Override
    public String nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        return node.asString();
    }

}
