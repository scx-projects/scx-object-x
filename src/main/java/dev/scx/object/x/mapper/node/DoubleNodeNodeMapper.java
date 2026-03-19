package dev.scx.object.x.mapper.node;

import dev.scx.node.DoubleNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// DoubleNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class DoubleNodeNodeMapper implements TypeNodeMapper<DoubleNode, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(DoubleNode.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(DoubleNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public DoubleNode nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 1, 处理 DoubleNode 类型
        if (node instanceof DoubleNode doubleNode) {
            return doubleNode.deepCopy();
        }
        // 2, 尝试转换
        try {
            return new DoubleNode(node.asDouble());
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

}
