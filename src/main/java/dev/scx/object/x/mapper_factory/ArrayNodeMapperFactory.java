package dev.scx.object.x.mapper_factory;

import dev.scx.object.x.TypeNodeMapperFactory;
import dev.scx.object.x.mapper.ArrayNodeMapper;
import dev.scx.reflect.ArrayTypeInfo;
import dev.scx.reflect.TypeInfo;

/// ArrayNodeMapperFactory
///
/// @author scx567888
/// @version 0.0.1
public final class ArrayNodeMapperFactory implements TypeNodeMapperFactory {

    @Override
    public ArrayNodeMapper createMapper(TypeInfo typeInfo) {
        // 只处理 数组
        if (typeInfo instanceof ArrayTypeInfo arrayTypeInfo) {
            return new ArrayNodeMapper(arrayTypeInfo);
        }
        return null;
    }

}
