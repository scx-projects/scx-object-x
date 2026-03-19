package dev.scx.object.x.mapper.primitive;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// CharNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class CharNodeMapper implements TypeNodeMapper<Character, StringNode> {

    private final boolean isPrimitive;

    public CharNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return isPrimitive ? typeOf(char.class) : typeOf(Character.class);
    }

    @Override
    public Class<StringNode> nodeType() {
        return StringNode.class;
    }

    @Override
    public StringNode valueToNode(Character value, ObjectToNodeContext context) {
        return new StringNode(value.toString());
    }

    @Override
    public Character nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        var text = node.asString();
        if (text.length() == 1) {
            return text.charAt(0);
        }
        throw new NodeToObjectException("String length must be 1 for char, got: " + text);
    }

    @Override
    public Character nullNodeToValue() throws NodeToObjectException {
        return isPrimitive ? (char) 0 : null;
    }

}
