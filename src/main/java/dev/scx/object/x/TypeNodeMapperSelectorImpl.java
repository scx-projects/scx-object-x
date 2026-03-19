package dev.scx.object.x;

import dev.scx.reflect.TypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static dev.scx.reflect.ScxReflect.typeOf;

/// TypeNodeMapperSelectorImpl (支持动态扩容)
///
/// @author scx567888
/// @version 0.0.1
public final class TypeNodeMapperSelectorImpl implements TypeNodeMapperSelector {

    // 同时缓存 Class 和 TypeInfo, 加速查找
    private final Map<Object, TypeNodeMapper<?, ?>> mappers;

    private final List<TypeNodeMapperFactory> mapperFactories;

    private final Lock lock;

    public TypeNodeMapperSelectorImpl() {
        this.mappers = new ConcurrentHashMap<>();
        this.mapperFactories = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    @Override
    public void registerMapper(TypeNodeMapper<?, ?> mapper) {
        var typeInfo = mapper.valueType();
        mappers.put(typeInfo, mapper);
        // 这里对可以无损 映射到 class 的进行双重 key 缓存.
        if (typeInfo.isRaw()) {
            mappers.put(typeInfo.rawClass(), mapper);
        }
    }

    @Override
    public void registerMapperFactory(TypeNodeMapperFactory mapperFactory) {
        mapperFactories.add(mapperFactory);
    }

    @Override
    public void registerMapperFactory(TypeNodeMapperFactory mapperFactory, int order) {
        mapperFactories.add(order, mapperFactory);
    }

    @Override
    public TypeNodeMapper<?, ?> findMapper(TypeInfo type) {
        var mapper = mappers.get(type);
        if (mapper != null) {
            return mapper;
        }
        lock.lock();
        try {
            // 双重检查
            mapper = mappers.get(type);
            if (mapper != null) {
                return mapper;
            }
            // 尝试创建 mapper
            mapper = this.tryCreateMapper(type);
            if (mapper != null) {
                // 注册到 mappers 中.
                registerMapper(mapper);
                return mapper;
            }
            // createMapper 也没有就是彻底没有了.
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> TypeNodeMapper<?, ?> findMapper(Class<T> type) {
        var mapper = mappers.get(type);
        if (mapper != null) {
            return mapper;
        }
        lock.lock();
        try {
            // 双重检查
            mapper = mappers.get(type);
            if (mapper != null) {
                return mapper;
            }
            // 尝试创建 mapper
            mapper = this.tryCreateMapper(typeOf(type));
            if (mapper != null) {
                // 注册到 mappers 中.
                registerMapper(mapper);
                return mapper;
            }
            // createMapper 也没有就是彻底没有了.
            return null;
        } finally {
            lock.unlock();
        }
    }

    /// 创建新的 NodeMapper
    private TypeNodeMapper<?, ?> tryCreateMapper(TypeInfo typeInfo) {
        for (var factory : mapperFactories) {
            var mapper = factory.createMapper(typeInfo);
            if (mapper != null) {
                return mapper;
            }
        }
        return null;
    }

}
