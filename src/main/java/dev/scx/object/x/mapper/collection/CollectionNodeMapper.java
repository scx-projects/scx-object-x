package dev.scx.object.x.mapper.collection;

import dev.scx.node.ArrayNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.util.Collection;

import static dev.scx.object.x.mapper.collection.CollectionNodeMapperHelper.createCollection;
import static dev.scx.object.x.mapper.collection.CollectionNodeMapperHelper.resolveComponentType;

/// CollectionNodeMapper
///
/// @author scx567888
public final class CollectionNodeMapper implements TypeNodeMapper<Collection<?>, ArrayNode> {

    private final ClassInfo classInfo;
    // 仅在 nodeToValue 时有用, 因为 valueToNode 时, 我们无法确定 value 的具体类型是否是 componentType
    private final TypeInfo componentType;
    // 性能优化: 缓存 componentType 对应的 TypeNodeMapper, 避免在每个元素转换时重复执行 findMapper.
    private TypeNodeMapper<?, ?> componentNodeMapper;

    CollectionNodeMapper(ClassInfo classInfo) {
        this.classInfo = classInfo;
        this.componentType = resolveComponentType(this.classInfo);
        this.componentNodeMapper = null;
    }

    @Override
    public TypeInfo valueType() {
        return classInfo;
    }

    @Override
    public Class<ArrayNode> nodeType(NodeToObjectContext context) {
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
        var result = createCollection(classInfo, node.size());
        for (var n : node) {
            var i = context.nodeToObject(n, componentNodeMapper);
            result.add(i);
        }
        return result;
    }

}
