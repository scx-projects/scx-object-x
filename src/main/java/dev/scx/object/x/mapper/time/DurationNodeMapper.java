package dev.scx.object.x.mapper.time;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.time.DateTimeException;
import java.time.Duration;

import static dev.scx.reflect.ScxReflect.typeOf;

/// DurationNodeMapper
///
/// @author scx567888
public final class DurationNodeMapper implements TypeNodeMapper<Duration, StringNode> {

    public DurationNodeMapper() {

    }

    @Override
    public TypeInfo valueType() {
        return typeOf(Duration.class);
    }

    @Override
    public Class<StringNode> nodeType(NodeToObjectContext context) {
        return StringNode.class;
    }

    @Override
    public StringNode valueToNode(Duration value, ObjectToNodeContext context) {
        return new StringNode(value.toString());
    }

    @Override
    public Duration nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return Duration.parse(node.asString());
        } catch (DateTimeException e) {
            throw new NodeToObjectException(e);
        }
    }

}
