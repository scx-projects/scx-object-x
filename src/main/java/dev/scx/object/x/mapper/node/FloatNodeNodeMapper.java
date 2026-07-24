package dev.scx.object.x.mapper.node;

import dev.scx.node.FloatNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.primitive.FloatNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// FloatNodeNodeMapper
///
/// @author scx567888
public final class FloatNodeNodeMapper implements TypeNodeMapper<FloatNode, ValueNode> {

    private final FloatNodeMapper floatNodeMapper;

    public FloatNodeNodeMapper() {
        this.floatNodeMapper = new FloatNodeMapper(true);
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(FloatNode.class);
    }

    @Override
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
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
        return new FloatNode(floatNodeMapper.nodeToValue(node, context));
    }

}
