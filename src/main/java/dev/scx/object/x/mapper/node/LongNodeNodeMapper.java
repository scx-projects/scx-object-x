package dev.scx.object.x.mapper.node;

import dev.scx.node.LongNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// LongNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class LongNodeNodeMapper implements TypeNodeMapper<LongNode, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(LongNode.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(LongNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public LongNode nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 1, 处理 LongNode 类型
        if (node instanceof LongNode longNode) {
            return longNode.deepCopy();
        }
        // 2, 尝试转换
        try {
            return new LongNode(node.asLong());
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

}
