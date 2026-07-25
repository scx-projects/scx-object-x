package dev.scx.object.x.mapper.time;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.function.Function;

import static dev.scx.reflect.ScxReflect.typeOf;

/// DurationNodeMapper
///
/// @author scx567888
public final class DurationNodeMapper implements TypeNodeMapper<Duration, StringNode> {

    private final Class<Duration> type;
    private final Function<Duration, String> generator;
    private final Function<String, Duration> parser;

    public DurationNodeMapper() {
        this.type = Duration.class;
        this.generator =  Duration::toString;
        this.parser = Duration::parse;
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(type);
    }

    @Override
    public Class<StringNode> nodeType(NodeToObjectContext context) {
        return StringNode.class;
    }

    @Override
    public StringNode valueToNode(Duration value, ObjectToNodeContext context) {
        return new StringNode(generator.apply(value));
    }

    @Override
    public Duration nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return parser.apply(node.asString());
        } catch (DateTimeException e) {
            throw new NodeToObjectException(e);
        }
    }

}
