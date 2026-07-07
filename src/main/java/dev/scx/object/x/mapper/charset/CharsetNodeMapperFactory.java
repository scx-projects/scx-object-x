package dev.scx.object.x.mapper.charset;

import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.TypeNodeMapperFactory;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.nio.charset.Charset;

/// CharsetNodeMapperFactory
///
/// @author scx567888
public final class CharsetNodeMapperFactory implements TypeNodeMapperFactory {

    @Override
    public TypeNodeMapper<?, ?> createMapper(TypeInfo typeInfo) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (Charset.class.isAssignableFrom(typeInfo.rawClass())) {
                return new CharsetNodeMapper(classInfo);
            }
        }
        return null;
    }

}
