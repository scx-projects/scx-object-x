package dev.scx.object.x;

import dev.scx.node.Node;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.reflect.TypeInfo;

/// 值 和 Node 的双向映射器
///
/// @author scx567888
/// @version 0.0.1
public interface TypeNodeMapper<V, N extends Node> {

    /// 值 的类型
    TypeInfo valueType();

    /// Node 的类型
    Class<N> nodeType();

    /// 将 值 转换为 Node.
    ///
    /// @param value   永不为 null
    /// @param context 映射上下文, 可用于递归或动态配置
    /// @return Node
    N valueToNode(V value, ObjectToNodeContext context) throws ObjectToNodeException;

    /// 将 Node 转换为 值.
    ///
    /// @param node    永不为 null, 永不为 NullNode.NULL
    /// @param context 映射上下文, 可用于递归或动态配置
    /// @return 值
    V nodeToValue(N node, NodeToObjectContext context) throws NodeToObjectException;

    /// 将 NullNode.NULL 转换为 值.
    ///
    /// @return 值
    default V nullNodeToValue() throws NodeToObjectException {
        // 默认 返回 null.
        return null;
    }

}
