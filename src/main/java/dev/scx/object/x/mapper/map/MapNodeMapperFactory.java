package dev.scx.object.x.mapper.map;

import dev.scx.object.x.mapper.TypeNodeMapperFactory;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.util.Map;

/// MapNodeMapperFactory
///
/// @author scx567888
public final class MapNodeMapperFactory implements TypeNodeMapperFactory {

    @Override
    public MapNodeMapper createMapper(TypeInfo typeInfo) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (Map.class.isAssignableFrom(typeInfo.rawClass())) {
                return new MapNodeMapper(classInfo);
            }
        }
        return null;
    }

}
