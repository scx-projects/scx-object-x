package dev.scx.object.x.context;

import dev.scx.node.Node;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.object.x.mapper.TypeNodeMapperOptions;
import dev.scx.reflect.TypeInfo;

/// Object -> Node 上下文
///
/// @author scx567888
public interface ObjectToNodeContext {

    Node objectToNode(Object value, Object pathSegment) throws ObjectToNodeException;

    Node objectToNode(Object value, Object pathSegment, TypeNodeMapper<?, ?> mapper) throws ObjectToNodeException;

    /// 找不到 抛 ObjectToNodeException 异常
    TypeNodeMapper<?, ?> findMapper(TypeInfo type) throws ObjectToNodeException;

    /// 找不到 抛 ObjectToNodeException 异常
    TypeNodeMapper<?, ?> findMapper(Class<?> type) throws ObjectToNodeException;

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
