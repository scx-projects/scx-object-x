package dev.scx.object.x.mapper.file;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.io.File;

import static dev.scx.reflect.ScxReflect.typeOf;

/// FileNodeMapper
///
/// @author scx567888
public final class FileNodeMapper implements TypeNodeMapper<File, StringNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(File.class);
    }

    @Override
    public Class<StringNode> nodeType() {
        return StringNode.class;
    }

    @Override
    public StringNode valueToNode(File value, ObjectToNodeContext context) throws ObjectToNodeException {
        return new StringNode(value.getPath());
    }

    @Override
    public File nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        var value = node.value();
        try {
            return new File(value);
        } catch (IllegalArgumentException e) {
            throw new NodeToObjectException(e);
        }
    }

}
