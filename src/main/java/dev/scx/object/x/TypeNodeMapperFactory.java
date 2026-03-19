package dev.scx.object.x;

import dev.scx.reflect.TypeInfo;

/// TypeNodeMapperFactory
///
/// @author scx567888
/// @version 0.0.1
public interface TypeNodeMapperFactory {

    /// 无法处理返回 null
    TypeNodeMapper<?, ?> createMapper(TypeInfo typeInfo);

}
