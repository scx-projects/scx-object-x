package dev.scx.object.x.mapper_factory;

import dev.scx.object.x.TypeNodeMapperFactory;
import dev.scx.object.x.mapper.EnumNodeMapper;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ClassKind;
import dev.scx.reflect.TypeInfo;

/// EnumNodeMapperFactory
///
/// @author scx567888
/// @version 0.0.1
public final class EnumNodeMapperFactory implements TypeNodeMapperFactory {

    @Override
    public EnumNodeMapper<?> createMapper(TypeInfo typeInfo) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (classInfo.classKind() == ClassKind.ENUM) {
                return new EnumNodeMapper<>(classInfo);
            }
        }
        return null;
    }

}
