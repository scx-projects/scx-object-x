package dev.scx.object.x.mapper.node;

import dev.scx.node.FloatNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// FloatNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class FloatNodeNodeMapper implements TypeNodeMapper<FloatNode, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(FloatNode.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(FloatNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public FloatNode nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 1, 处理 FloatNode 类型
        if (node instanceof FloatNode floatNode) {
            return floatNode.deepCopy();
        }
        // 2, 尝试转换
        try {
            return new FloatNode(node.asFloat());
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

}
