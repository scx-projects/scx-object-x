package dev.scx.object.x.mapper.collection;

import dev.scx.object.x.mapper.TypeNodeMapperFactory;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.util.Collection;

/// CollectionNodeMapperFactory
///
/// @author scx567888
public final class CollectionNodeMapperFactory implements TypeNodeMapperFactory {

    @Override
    public CollectionNodeMapper createMapper(TypeInfo typeInfo) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (Collection.class.isAssignableFrom(typeInfo.rawClass())) {
                return new CollectionNodeMapper(classInfo);
            }
        }
        return null;
    }

}
