package dev.scx.object.x.mapper.path;

import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.TypeNodeMapperFactory;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.nio.file.Path;

/// PathNodeMapperFactory
///
/// @author scx567888
public final class PathNodeMapperFactory implements TypeNodeMapperFactory {

    @Override
    public TypeNodeMapper<?, ?> createMapper(TypeInfo typeInfo) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (Path.class.isAssignableFrom(typeInfo.rawClass())) {
                return new PathNodeMapper(classInfo);
            }
        }
        return null;
    }

}
