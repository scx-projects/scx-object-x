package dev.scx.object.x;

import dev.scx.reflect.TypeInfo;

public interface TypeNodeMapperSelector {

    void registerMapper(TypeNodeMapper<?, ?> mapper);

    void registerMapperFactory(TypeNodeMapperFactory mapperFactory);

    void registerMapperFactory(TypeNodeMapperFactory mapperFactory, int order);

    /// 没找到会返回 null
    TypeNodeMapper<?, ?> findMapper(TypeInfo type);

    /// 没找到会返回 null
    <T> TypeNodeMapper<?, ?> findMapper(Class<T> type);

}
