package dev.scx.object.x.mapper.node;

import dev.scx.node.IntNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.primitive.IntNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// IntNodeNodeMapper
///
/// @author scx567888
public final class IntNodeNodeMapper implements TypeNodeMapper<IntNode, ValueNode> {

    private final IntNodeMapper intNodeMapper;

    public IntNodeNodeMapper() {
        this.intNodeMapper = new IntNodeMapper(true);
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(IntNode.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(IntNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public IntNode nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 1, 处理 IntNode 类型
        if (node instanceof IntNode intNode) {
            return intNode.deepCopy();
        }
        // 2, 尝试转换
        return new IntNode(intNodeMapper.nodeToValue(node, context));
    }

}
