package dev.scx.object.x.mapper;

import dev.scx.node.*;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.ArrayTypeInfo;
import dev.scx.reflect.TypeInfo;

/// ArrayNodeMapper
///
/// 支持 基本类型数组和 Object[]
///
/// @author scx567888
/// @version 0.0.1
public final class ArrayNodeMapper implements TypeNodeMapper<Object, ArrayNode> {

    private final ArrayTypeInfo arrayTypeInfo;
    // 这个只能用于 nodeToValue, 因为 valueToNode 存在数组协变.
    private final TypeInfo componentType;
    // 性能优化: 缓存 componentType 对应的 TypeNodeMapper, 避免在每个元素转换时重复执行 findMapper.
    private TypeNodeMapper<?, ?> componentNodeMapper;

    public ArrayNodeMapper(ArrayTypeInfo arrayTypeInfo) {
        this.arrayTypeInfo = arrayTypeInfo;
        this.componentType = arrayTypeInfo.componentType();
        this.componentNodeMapper = null;
    }

    @Override
    public TypeInfo valueType() {
        return arrayTypeInfo;
    }

    @Override
    public Class<ArrayNode> nodeType() {
        return ArrayNode.class;
    }

    @Override
    public ArrayNode valueToNode(Object value, ObjectToNodeContext context) throws ObjectToNodeException {
        switch (value) {
            // 这里针对 8 个基本类型 进行性能优化 因为他们不存在协变的可能.
            case byte[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(new IntNode(a));
                }
                return arrayNode;
            }
            case short[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(new IntNode(a));
                }
                return arrayNode;
            }
            case int[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(new IntNode(a));
                }
                return arrayNode;
            }
            case long[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(new LongNode(a));
                }
                return arrayNode;
            }
            case float[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(new FloatNode(a));
                }
                return arrayNode;
            }
            case double[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(new DoubleNode(a));
                }
                return arrayNode;
            }
            case boolean[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(BooleanNode.of(a));
                }
                return arrayNode;
            }
            case char[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(new StringNode(String.valueOf(a)));
                }
                return arrayNode;
            }
            // 处理 Object[]
            case Object[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                var i = 0;
                for (var a : arr) {
                    // 我们不能确定 每个数组元素一定是 componentNodeMapper 支持的类型
                    // 所以这里 需要 递归调用 context.objectToNode
                    // 但是对于基本类型则不需要
                    arrayNode.add(context.objectToNode(a, i));
                    i = i + 1;
                }
                return arrayNode;
            }
            // 不是数组类型, 这里基本上是不可达的, 如果不是外部直接调用的话
            default -> throw new ObjectToNodeException("Unsupported type: " + value.getClass());
        }
    }

    @Override
    public Object nodeToValue(ArrayNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 0, 性能优化.
        if (componentNodeMapper == null) {
            componentNodeMapper = context.findMapper(componentType);
        }

        // 这里需要特殊循环处理
        var array = arrayTypeInfo.newArray(node.size());

        switch (array) {
            // 基本类型
            case byte[] result -> {
                var i = 0;
                for (var e : node) {
                    result[i] = context.nodeToObject(e, componentNodeMapper);
                    i = i + 1;
                }
                return result;
            }
            case short[] result -> {
                var i = 0;
                for (var e : node) {
                    result[i] = context.nodeToObject(e, componentNodeMapper);
                    i = i + 1;
                }
                return result;
            }
            case int[] result -> {
                var i = 0;
                for (var e : node) {
                    result[i] = context.nodeToObject(e, componentNodeMapper);
                    i = i + 1;
                }
                return result;
            }
            case long[] result -> {
                var i = 0;
                for (var e : node) {
                    result[i] = context.nodeToObject(e, componentNodeMapper);
                    i = i + 1;
                }
                return result;
            }
            case float[] result -> {
                var i = 0;
                for (var e : node) {
                    result[i] = context.nodeToObject(e, componentNodeMapper);
                    i = i + 1;
                }
                return result;
            }
            case double[] result -> {
                var i = 0;
                for (var e : node) {
                    result[i] = context.nodeToObject(e, componentNodeMapper);
                    i = i + 1;
                }
                return result;
            }
            case boolean[] result -> {
                var i = 0;
                for (var e : node) {
                    result[i] = context.nodeToObject(e, componentNodeMapper);
                    i = i + 1;
                }
                return result;
            }
            case char[] result -> {
                var i = 0;
                for (var e : node) {
                    result[i] = context.nodeToObject(e, componentNodeMapper);
                    i = i + 1;
                }
                return result;
            }
            // Object[]
            case Object[] result -> {
                var i = 0;
                for (var e : node) {
                    result[i] = context.nodeToObject(e, componentNodeMapper);
                    i = i + 1;
                }
                return result;
            }
            // 这里应该不可达
            default -> throw new NodeToObjectException("Unsupported type: " + array.getClass());
        }
    }

}
