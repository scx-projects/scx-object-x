package dev.scx.object.x.mapper.time;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAmount;
import java.util.function.Function;

import static dev.scx.reflect.ScxReflect.typeOf;

/// TemporalAmountNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class TemporalAmountNodeMapper<T extends TemporalAmount> implements TypeNodeMapper<T, StringNode> {

    private final Class<T> type;
    private final Function<T, String> generator;
    private final Function<String, T> parser;

    public TemporalAmountNodeMapper(Class<T> type, Function<T, String> generator, Function<String, T> parser) {
        this.type = type;
        this.generator = generator;
        this.parser = parser;
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(type);
    }

    @Override
    public Class<StringNode> nodeType() {
        return StringNode.class;
    }

    @Override
    public StringNode valueToNode(T value, ObjectToNodeContext context) {
        return new StringNode(generator.apply(value));
    }

    @Override
    public T nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return parser.apply(node.asString());
        } catch (DateTimeParseException e) {
            throw new NodeToObjectException(e);
        }
    }

}
