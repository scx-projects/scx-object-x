package dev.scx.object.x.mapper_factory;

import dev.scx.object.x.TypeNodeMapperFactory;
import dev.scx.object.x.mapper.collection.CollectionNodeMapper;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.util.Collection;

import static dev.scx.reflect.ScxReflect.typeOf;

/// CollectionNodeMapperFactory
///
/// @author scx567888
/// @version 0.0.1
public final class CollectionNodeMapperFactory implements TypeNodeMapperFactory {

    @Override
    public CollectionNodeMapper createMapper(TypeInfo typeInfo) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (Collection.class.isAssignableFrom(typeInfo.rawClass())) {
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
                return new CollectionNodeMapper(classInfo, componentType);
            }
        }
        return null;
    }

}
