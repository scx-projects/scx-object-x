package dev.scx.object.x.mapper.node;

import dev.scx.node.BigIntegerNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.math.BigIntegerNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// BigIntegerNodeNodeMapper
///
/// @author scx567888
public final class BigIntegerNodeNodeMapper implements TypeNodeMapper<BigIntegerNode, ValueNode> {

    private final BigIntegerNodeMapper bigIntegerNodeMapper;

    public BigIntegerNodeNodeMapper() {
        this.bigIntegerNodeMapper = new BigIntegerNodeMapper();
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(BigIntegerNode.class);
    }

    @Override
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
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
        return new BigIntegerNode(bigIntegerNodeMapper.nodeToValue(node, context));
    }

}
