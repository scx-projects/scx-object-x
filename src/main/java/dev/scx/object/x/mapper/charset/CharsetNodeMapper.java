package dev.scx.object.x.mapper.charset;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

/// CharsetNodeMapper
///
/// @author scx567888
public final class CharsetNodeMapper implements TypeNodeMapper<Charset, StringNode> {

    private final ClassInfo classInfo;

    public CharsetNodeMapper(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public TypeInfo valueType() {
        return classInfo;
    }

    @Override
    public Class<StringNode> nodeType() {
        return StringNode.class;
    }

    @Override
    public StringNode valueToNode(Charset value, ObjectToNodeContext context) throws ObjectToNodeException {
        return new StringNode(value.name());
    }

    @Override
    public Charset nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        var value = node.value();
        try {
            return Charset.forName(value);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            throw new NodeToObjectException(e);
        }
    }

}
