package dev.scx.object.x.mapper.node;

import dev.scx.node.StringNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// StringNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class StringNodeNodeMapper implements TypeNodeMapper<StringNode, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(StringNode.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(StringNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public StringNode nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 1, 处理 StringNode 类型
        if (node instanceof StringNode stringNode) {
            return stringNode.deepCopy();
        }
        // 2, 尝试转换
        return new StringNode(node.asString());
    }

}
