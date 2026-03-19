package dev.scx.object.x.mapper;

import dev.scx.node.*;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static dev.scx.reflect.ScxReflect.typeOf;

/// 未指定类型的 Object.class
///
/// @author scx567888
/// @version 0.0.1
public final class UntypedNodeMapper implements TypeNodeMapper<Object, Node> {

    @Override
    public TypeInfo valueType() {
        return typeOf(Object.class);
    }

    @Override
    public Class<Node> nodeType() {
        return Node.class;
    }

    @Override
    public Node valueToNode(Object value, ObjectToNodeContext context) {
        // 实际上 只是一个空的 Object 对象, 我们 转换为一个空的 ObjectNode
        return new ObjectNode();
    }

    @Override
    public Object nodeToValue(Node node, NodeToObjectContext context) throws NodeToObjectException {
        // 我们根据 最适合的类型来处理
        return switch (node) {
            // 这个分支理论上不会发生.
            case NullNode _ -> null;
            // ObjectNode 就用 Map
            case ObjectNode _ -> context.nodeToObject(node, Map.class);
            // ArrayNode 就用 List
            case ArrayNode _ -> context.nodeToObject(node, List.class);
            // 其余字面量选择最接近的
            case IntNode _ -> context.nodeToObject(node, Integer.class);
            case LongNode _ -> context.nodeToObject(node, Long.class);
            case FloatNode _ -> context.nodeToObject(node, Float.class);
            case DoubleNode _ -> context.nodeToObject(node, Double.class);
            case BigIntegerNode _ -> context.nodeToObject(node, BigInteger.class);
            case BigDecimalNode _ -> context.nodeToObject(node, BigDecimal.class);
            case StringNode _ -> context.nodeToObject(node, String.class);
            case BooleanNode _ -> context.nodeToObject(node, Boolean.class);
        };
    }

}
