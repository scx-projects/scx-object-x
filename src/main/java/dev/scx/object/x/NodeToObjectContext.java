package dev.scx.object.x;

import dev.scx.node.Node;
import dev.scx.object.NodeToObjectException;
import dev.scx.reflect.TypeInfo;

/// Node -> Object 上下文
///
/// @author scx567888
/// @version 0.0.1
public interface NodeToObjectContext {

    <T> T nodeToObject(Node node, TypeInfo type) throws NodeToObjectException;

    <T> T nodeToObject(Node node, Class<T> clazz) throws NodeToObjectException;

    <T> T nodeToObject(Node node, TypeNodeMapper<?, ?> mapper) throws NodeToObjectException;

    /// 找不到 抛 NodeToObjectException 异常
    TypeNodeMapper<?, ?> findMapper(TypeInfo type) throws NodeToObjectException;

    /// 找不到 抛 NodeToObjectException 异常
    TypeNodeMapper<?, ?> findMapper(Class<?> type) throws NodeToObjectException;

    /// 找不到返回 null
    <T extends TypeNodeMapperOptions> T getMapperOptions(Class<T> optionsType);

    /// 找不到返回 默认值
    default <T extends TypeNodeMapperOptions> T getMapperOptions(Class<T> optionsType, T defaultOptions) {
        T mapperOptions = getMapperOptions(optionsType);
        if (mapperOptions == null) {
            return defaultOptions;
        }
        return mapperOptions;
    }

}
