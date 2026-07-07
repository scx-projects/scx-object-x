package dev.scx.object.x.mapper.collection;

import dev.scx.object.NodeToObjectException;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.util.*;

import static dev.scx.reflect.ScxReflect.typeOf;

public final class CollectionNodeMapperHelper {

    public static TypeInfo resolveComponentType(ClassInfo classInfo) {
        var collectionType = classInfo.findSuperType(Collection.class);
        if (collectionType == null) {
            // 理论上不可能发生
            throw new IllegalStateException("Collection class not found");
        }
        // 尝试获取 componentType
        var componentType = collectionType.bindings().get("E");
        // 为 null 回退到 Object
        if (componentType == null) {
            componentType = typeOf(Object.class);
        }
        // 这个 componentType 实际上只能用于 nodeToValue,
        // 因为在 valueToNode 的时候,由于 Collection 的泛型是被擦除的,
        // 所以我们是不能假定 每一个元素的类型都真的是 componentType
        return componentType;
    }

    public static Collection<Object> createCollection(ClassInfo classInfo, int size) throws NodeToObjectException {
        if (classInfo.rawClass() == Collection.class) {
            return new ArrayList<>(size);
        }
        // 如果只是 List 那么我们需一个默认的实现 这里使用 ArrayList
        if (classInfo.rawClass() == List.class) {
            return new ArrayList<>(size);
        }
        if (classInfo.rawClass() == Set.class) {
            return new HashSet<>(size);
        }
        if (classInfo.rawClass() == ArrayList.class) {
            return new ArrayList<>(size);
        }
        if (classInfo.rawClass() == LinkedList.class) {
            return new LinkedList<>();
        }
        if (classInfo.rawClass() == HashSet.class) {
            return new HashSet<>(size);
        }
        if (classInfo.rawClass() == TreeSet.class) {
            return new TreeSet<>();
        }
        if (classInfo.rawClass() == LinkedHashSet.class) {
            return new LinkedHashSet<>(size);
        }
        throw new NodeToObjectException("Unsupported Collection type: " + classInfo);
    }

}
