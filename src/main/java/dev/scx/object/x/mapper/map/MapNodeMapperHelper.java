package dev.scx.object.x.mapper.map;

import dev.scx.object.NodeToObjectException;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import static dev.scx.reflect.ScxReflect.typeOf;

public final class MapNodeMapperHelper {

    public static TypeInfo resolveKeyType(ClassInfo classInfo) {
        var mapType = classInfo.findSuperType(Map.class);
        if (mapType == null) {
            // 理论上不可能发生
            throw new IllegalStateException("Map class not found");
        }
        var keyType = mapType.bindings().get("K");
        // 为 null 回退到 String
        if (keyType == null) {
            keyType = typeOf(String.class);
        }
        // 这个 keyType 实际上只能用于 nodeToValue,
        // 因为在 valueToNode 的时候, 由于 Map 的泛型是被擦除的,
        // 所以我们是不能假定 每一个元素的类型都真的是 keyType
        return keyType;
    }

    public static TypeInfo resolveValueType(ClassInfo classInfo) {
        var mapType = classInfo.findSuperType(Map.class);
        if (mapType == null) {
            // 理论上不可能发生
            throw new IllegalStateException("Map class not found");
        }
        // 为 null 回退到 Object
        var valueType = mapType.bindings().get("V");
        if (valueType == null) {
            valueType = typeOf(Object.class);
        }
        // 这个 valueType 实际上只能用于 nodeToValue,
        // 因为在 valueToNode 的时候, 由于 Map 的泛型是被擦除的,
        // 所以我们是不能假定 每一个元素的类型都真的是 valueType
        return valueType;
    }

    public static Map<Object, Object> createMap(ClassInfo classInfo, int size) throws NodeToObjectException {
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
