package dev.scx.object.x.mapper.path;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/// PathNodeMapper
///
/// @author scx567888
public final class PathNodeMapper implements TypeNodeMapper<Path, StringNode> {

    private final ClassInfo classInfo;

    public PathNodeMapper(ClassInfo classInfo) {
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
    public StringNode valueToNode(Path value, ObjectToNodeContext context) throws ObjectToNodeException {
        return new StringNode(value.toString());
    }

    @Override
    public Path nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        var value = node.value();
        try {
            return Path.of(value);
        } catch (InvalidPathException e) {
            throw new NodeToObjectException(e);
        }
    }

}
