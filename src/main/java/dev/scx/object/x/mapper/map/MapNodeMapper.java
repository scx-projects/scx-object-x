package dev.scx.object.x.mapper.map;

import dev.scx.node.ObjectNode;
import dev.scx.node.StringNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import static dev.scx.node.NullNode.NULL;

/// MapNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class MapNodeMapper implements TypeNodeMapper<Map<?, ?>, ObjectNode> {

    private static final MapNodeMapperOptions MAP_NODE_MAPPER_OPTIONS = new MapNodeMapperOptions();

    private final ClassInfo classInfo;
    // 只在 nodeToValue 时有用, 因为 valueToNode 时, 我们无法确定 key 的具体类型是否是 keyType.
    private final TypeInfo keyType;
    // 只在 nodeToValue 时有用, 因为 valueToNode 时, 我们无法确定 value 的具体类型是否是 valueType.
    private final TypeInfo valueType;
    // 性能优化: 缓存 keyType 对应的 TypeNodeMapper, 避免在每个元素转换时重复执行 findMapper.
    private TypeNodeMapper<?, ?> keyNodeMapper;
    // 性能优化: 缓存 valueType 对应的 TypeNodeMapper, 避免在每个元素转换时重复执行 findMapper.
    private TypeNodeMapper<?, ?> valueNodeMapper;

    public MapNodeMapper(ClassInfo classInfo, TypeInfo keyType, TypeInfo valueType) {
        this.classInfo = classInfo;
        this.keyType = keyType;
        this.valueType = valueType;
        this.keyNodeMapper = null;
        this.valueNodeMapper = null;
    }

    @Override
    public TypeInfo valueType() {
        return classInfo;
    }

    @Override
    public Class<ObjectNode> nodeType() {
        return ObjectNode.class;
    }

    @Override
    public ObjectNode valueToNode(Map<?, ?> mapValue, ObjectToNodeContext context) throws ObjectToNodeException {
        var options = context.getMapperOptions(MapNodeMapperOptions.class, MAP_NODE_MAPPER_OPTIONS);
        var objectNode = new ObjectNode(mapValue.size());
        for (var e : mapValue.entrySet()) {
            var key = e.getKey();
            var value = e.getValue();
            // 处理忽略 null value
            if (value == null && options.ignoreNullValue()) {
                continue;
            }
            var k = toKey(key, context, options);
            var v = context.objectToNode(value, k);
            objectNode.put(k, v);
        }
        return objectNode;
    }

    @Override
    public Map<?, ?> nodeToValue(ObjectNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 0, 性能优化.
        if (keyNodeMapper == null) {
            keyNodeMapper = context.findMapper(keyType);
        }
        if (valueNodeMapper == null) {
            valueNodeMapper = context.findMapper(valueType);
        }
        // 转换
        var result = createMap(node.size());
        for (var n : node) {
            // 此处需要将 String 包装为 StringNode
            var k = context.nodeToObject(new StringNode(n.getKey()), keyNodeMapper);
            var v = context.nodeToObject(n.getValue(), valueNodeMapper);
            result.put(k, v);
        }
        return result;
    }

    private String toKey(Object value, ObjectToNodeContext context, MapNodeMapperOptions options) throws ObjectToNodeException {
        // 1, 尝试将 key 转换为 String
        var node = context.objectToNode(value, "<key>");
        // 2, 处理 nullKey
        if (node == NULL) {
            return options.nullKey();
        }
        // 3, 处理 ValueNode
        if (node instanceof ValueNode valueNode) {
            return valueNode.asString();
        }
        // 4, 其余类型直接报错
        throw new ObjectToNodeException(
            "Map key must be converted to ValueNode, but got: " + node.getClass().getName()
        );
    }

    private Map<Object, Object> createMap(int size) throws NodeToObjectException {
        // 如果只是 Map 那么我们需一个默认的实现 这里使用 LinkedHashMap
        if (classInfo.rawClass() == Map.class) {
            return new LinkedHashMap<>(size);
        }
        if (classInfo.rawClass() == HashMap.class) {
            return new HashMap<>(size);
        }
        if (classInfo.rawClass() == LinkedHashMap.class) {
            return new LinkedHashMap<>(size);
        }
        if (classInfo.rawClass() == TreeMap.class) {
            return new TreeMap<>();
        }
        if (classInfo.rawClass() == ConcurrentHashMap.class) {
            return new ConcurrentHashMap<>(size);
        }
        throw new NodeToObjectException("Unsupported Map type: " + classInfo);
    }

}
