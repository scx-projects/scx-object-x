package dev.scx.object.x.mapper;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

/// EnumNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class EnumNodeMapper<E extends Enum<E>> implements TypeNodeMapper<E, StringNode> {

    private final ClassInfo enumType;
    private final Class<E> enumClass;

    @SuppressWarnings("unchecked")
    public EnumNodeMapper(ClassInfo enumType) {
        this.enumType = enumType;
        this.enumClass = (Class<E>) this.enumType.enumClass().rawClass();
    }

    @Override
    public TypeInfo valueType() {
        return enumType;
    }

    @Override
    public Class<StringNode> nodeType() {
        return StringNode.class;
    }

    @Override
    public StringNode valueToNode(E value, ObjectToNodeContext context) {
        return new StringNode(value.name());
    }

    @Override
    public E nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        var value = node.value();
        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            throw new NodeToObjectException(e);
        }
    }

}
