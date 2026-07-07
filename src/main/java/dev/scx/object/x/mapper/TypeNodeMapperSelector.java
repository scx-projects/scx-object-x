package dev.scx.object.x.mapper;

import dev.scx.reflect.TypeInfo;

/// TypeNodeMapperSelector
///
/// - findMapper 保证线程安全.
///
/// @author scx567888
public interface TypeNodeMapperSelector {

    void registerMapper(TypeNodeMapper<?, ?> mapper);

    void registerMapperFactory(TypeNodeMapperFactory mapperFactory);

    void registerMapperFactory(TypeNodeMapperFactory mapperFactory, int order);

    /// 没找到会返回 null
    TypeNodeMapper<?, ?> findMapper(TypeInfo type);

    /// 没找到会返回 null
    TypeNodeMapper<?, ?> findMapper(Class<?> type);

}
