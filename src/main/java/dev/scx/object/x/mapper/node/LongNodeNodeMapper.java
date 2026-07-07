package dev.scx.object.x.mapper.node;

import dev.scx.node.LongNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.primitive.LongNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// LongNodeNodeMapper
///
/// @author scx567888
public final class LongNodeNodeMapper implements TypeNodeMapper<LongNode, ValueNode> {

    private final LongNodeMapper longNodeMapper;

    public LongNodeNodeMapper() {
        this.longNodeMapper = new LongNodeMapper(true);
    }

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
        return new LongNode(longNodeMapper.nodeToValue(node, context));
    }

}
