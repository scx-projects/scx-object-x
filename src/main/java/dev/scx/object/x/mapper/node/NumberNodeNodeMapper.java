package dev.scx.object.x.mapper.node;

import dev.scx.node.IntNode;
import dev.scx.node.NumberNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// NumericNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class NumberNodeNodeMapper implements TypeNodeMapper<NumberNode, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(NumberNode.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(NumberNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public NumberNode nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 1, 处理 NumberNode 类型
        if (node instanceof NumberNode numberNode) {
            return numberNode.deepCopy();
        }
        // 2, 尝试转换 我们不知道 ValueNode 到底能转成什么, 这里假设是 int
        try {
            return new IntNode(node.asInt());
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

}
