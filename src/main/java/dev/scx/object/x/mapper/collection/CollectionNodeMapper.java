package dev.scx.object.x.mapper.collection;

import dev.scx.node.ArrayNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.util.*;

/// CollectionNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class CollectionNodeMapper implements TypeNodeMapper<Collection<?>, ArrayNode> {

    private final ClassInfo classInfo;
    // 仅在 nodeToValue 时有用, 因为 valueToNode 时, 我们无法确定 value 的具体类型是否是 componentType
    private final TypeInfo componentType;
    // 性能优化: 缓存 componentType 对应的 TypeNodeMapper, 避免在每个元素转换时重复执行 findMapper.
    private TypeNodeMapper<?, ?> componentNodeMapper;

    public CollectionNodeMapper(ClassInfo classInfo, TypeInfo componentType) {
        this.classInfo = classInfo;
        this.componentType = componentType;
        this.componentNodeMapper = null;
    }

    @Override
    public TypeInfo valueType() {
        return classInfo;
    }

    @Override
    public Class<ArrayNode> nodeType() {
        return ArrayNode.class;
    }

    @Override
    public ArrayNode valueToNode(Collection<?> value, ObjectToNodeContext context) throws ObjectToNodeException {
        var arrayNode = new ArrayNode(value.size());
        var i = 0;
        for (var a : value) {
            arrayNode.add(context.objectToNode(a, i));
            i = i + 1;
        }
        return arrayNode;
    }

    @Override
    public Collection<?> nodeToValue(ArrayNode node, NodeToObjectContext context) throws NodeToObjectException {
        // 0, 性能优化.
        if (componentNodeMapper == null) {
            componentNodeMapper = context.findMapper(componentType);
        }
        // 转换
        var result = createCollection(node.size());
        for (var n : node) {
            var i = context.nodeToObject(n, componentNodeMapper);
            result.add(i);
        }
        return result;
    }

    private Collection<Object> createCollection(int size) throws NodeToObjectException {
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
