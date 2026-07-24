package dev.scx.object.x.mapper.byte_array;

import dev.scx.node.ArrayNode;
import dev.scx.node.Node;
import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.ArrayNodeMapper;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.ArrayTypeInfo;
import dev.scx.reflect.TypeInfo;

import java.util.Base64;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ByteArrayNodeMapper
///
/// @author scx567888
public final class ByteArrayNodeMapper implements TypeNodeMapper<byte[], Node> {

    private static final ByteArrayNodeMapperOptions BYTE_ARRAY_NODE_MAPPER_OPTIONS = new ByteArrayNodeMapperOptions();

    private final ArrayNodeMapper arrayNodeMapper;

    public ByteArrayNodeMapper() {
        this.arrayNodeMapper = new ArrayNodeMapper((ArrayTypeInfo) typeOf(byte[].class));
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(byte[].class);
    }

    @Override
    public Class<? extends Node> nodeType(NodeToObjectContext context) {
        var options = context.getMapperOptions(ByteArrayNodeMapperOptions.class, BYTE_ARRAY_NODE_MAPPER_OPTIONS);
        if (options.useBase64()) {
            return StringNode.class;
        } else {
            return ArrayNode.class;
        }
    }

    @Override
    public Node valueToNode(byte[] value, ObjectToNodeContext context) {
        var options = context.getMapperOptions(ByteArrayNodeMapperOptions.class, BYTE_ARRAY_NODE_MAPPER_OPTIONS);
        if (options.useBase64()) {
            var base64 = Base64.getEncoder().encodeToString(value);
            return new StringNode(base64);
        } else {
            return arrayNodeMapper.valueToNode(value, context);
        }
    }

    @Override
    public byte[] nodeToValue(Node node, NodeToObjectContext context) throws NodeToObjectException {
        var options = context.getMapperOptions(ByteArrayNodeMapperOptions.class, BYTE_ARRAY_NODE_MAPPER_OPTIONS);
        if (options.useBase64()) {
            if (!(node instanceof StringNode stringNode)) {
                // 这里理论上不会发生, 异常信息和 NodeToObjectContextImpl 保持一致
                throw new NodeToObjectException("Node type mismatch, expected: " + StringNode.class.getName() + ", got: " + node.getClass().getName());
            }

            // 尝试转换
            try {
                return Base64.getDecoder().decode(stringNode.value());
            } catch (IllegalArgumentException e) {
                throw new NodeToObjectException(e);
            }

        } else {
            if (!(node instanceof ArrayNode arrayNode)) {
                // 这里理论上不会发生, 异常信息和 NodeToObjectContextImpl 保持一致
                throw new NodeToObjectException("Node type mismatch, expected: " + ArrayNode.class.getName() + ", got: " + node.getClass().getName());
            }
            return (byte[]) arrayNodeMapper.nodeToValue(arrayNode, context);
        }
    }

}
