package dev.scx.object.x.mapper.node;

import dev.scx.node.BooleanNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// BooleanNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class BooleanNodeNodeMapper implements TypeNodeMapper<BooleanNode, ValueNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(BooleanNode.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(BooleanNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public BooleanNode nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 1, 处理 BooleanNode 类型
        if (node instanceof BooleanNode booleanNode) {
            return booleanNode.deepCopy();
        }
        // 2, 尝试转换
        return BooleanNode.of(node.asBoolean());
    }

}
