package dev.scx.object.x.mapper.node;

import dev.scx.node.BigDecimalNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.math.BigDecimalNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// BigDecimalNodeNodeMapper
///
/// @author scx567888
public final class BigDecimalNodeNodeMapper implements TypeNodeMapper<BigDecimalNode, ValueNode> {

    private final BigDecimalNodeMapper bigDecimalNodeMapper;

    public BigDecimalNodeNodeMapper() {
        this.bigDecimalNodeMapper = new BigDecimalNodeMapper();
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(BigDecimalNode.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(BigDecimalNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public BigDecimalNode nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 1, 处理 BigDecimalNode 类型
        if (node instanceof BigDecimalNode bigDecimalNode) {
            return bigDecimalNode.deepCopy();
        }
        // 2, 尝试转换
        return new BigDecimalNode(bigDecimalNodeMapper.nodeToValue(node, context));
    }

}
