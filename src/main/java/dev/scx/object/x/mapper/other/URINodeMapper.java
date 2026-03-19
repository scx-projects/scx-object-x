package dev.scx.object.x.mapper.other;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.net.URI;

import static dev.scx.reflect.ScxReflect.typeOf;

/// URINodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class URINodeMapper implements TypeNodeMapper<URI, StringNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(URI.class);
    }

    @Override
    public Class<StringNode> nodeType() {
        return StringNode.class;
    }

    @Override
    public StringNode valueToNode(URI value, ObjectToNodeContext context) throws ObjectToNodeException {
        return new StringNode(value.toString());
    }

    @Override
    public URI nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        var value = node.value();
        try {
            return URI.create(value);
        } catch (IllegalArgumentException e) {
            throw new NodeToObjectException(e);
        }
    }

}
