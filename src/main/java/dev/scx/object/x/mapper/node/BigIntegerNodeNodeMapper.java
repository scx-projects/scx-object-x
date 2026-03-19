package dev.scx.object.x.mapper.node;

import dev.scx.node.BigIntegerNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// BigIntegerNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class BigIntegerNodeNodeMapper implements TypeNodeMapper<BigIntegerNode, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(BigIntegerNode.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(BigIntegerNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public BigIntegerNode nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 1, 处理 BigIntegerNode 类型
        if (node instanceof BigIntegerNode bigIntegerNode) {
            return bigIntegerNode.deepCopy();
        }
        // 2, 尝试转换
        try {
            return new BigIntegerNode(node.asBigInteger());
        } catch (NumberFormatException e) {
            throw new NodeToObjectException(e);
        }
    }

}
