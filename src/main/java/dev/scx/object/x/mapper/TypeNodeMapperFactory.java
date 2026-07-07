package dev.scx.object.x.mapper;

import dev.scx.reflect.TypeInfo;

/// TypeNodeMapperFactory
///
/// @author scx567888
public interface TypeNodeMapperFactory {

    /// 无法处理返回 null
    TypeNodeMapper<?, ?> createMapper(TypeInfo typeInfo);

}
