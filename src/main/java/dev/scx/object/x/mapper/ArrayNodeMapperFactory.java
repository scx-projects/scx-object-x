package dev.scx.object.x.mapper;

import dev.scx.reflect.ArrayTypeInfo;
import dev.scx.reflect.TypeInfo;

/// ArrayNodeMapperFactory
///
/// @author scx567888
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
