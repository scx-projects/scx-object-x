package dev.scx.object.x.mapper.time;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.time.DateTimeException;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.function.Function;

import static dev.scx.reflect.ScxReflect.typeOf;

/// PeriodNodeMapper
///
/// @author scx567888
public final class PeriodNodeMapper implements TypeNodeMapper<Period, StringNode> {

    private final Class<Period> type;
    private final Function<Period, String> generator;
    private final Function<String, Period> parser;

    public PeriodNodeMapper() {
        this.type = Period.class;
        this.generator = Period::toString;
        this.parser = Period::parse;
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
    public StringNode valueToNode(Period value, ObjectToNodeContext context) {
        return new StringNode(generator.apply(value));
    }

    @Override
    public Period nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return parser.apply(node.asString());
        } catch (DateTimeException e) {
            throw new NodeToObjectException(e);
        }
    }

}
