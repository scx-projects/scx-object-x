package dev.scx.object.x.mapper.time;

import dev.scx.node.LongNode;
import dev.scx.node.StringNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;

import static dev.scx.reflect.ScxReflect.typeOf;

/// TemporalAccessorNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class TemporalAccessorNodeMapper<T extends TemporalAccessor> implements TypeNodeMapper<T, ValueNode> {

    private static final TemporalAccessorNodeMapperOptions TEMPORAL_ACCESSOR_NODE_MAPPER_OPTIONS = new TemporalAccessorNodeMapperOptions();

    private final Class<? extends TemporalAccessor> type;
    private final TemporalQuery<T> temporalQuery;

    public TemporalAccessorNodeMapper(Class<? extends TemporalAccessor> type, TemporalQuery<T> temporalQuery) {
        this.type = type;
        this.temporalQuery = temporalQuery;
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(type);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(T value, ObjectToNodeContext context) {
        var options = context.getMapperOptions(TemporalAccessorNodeMapperOptions.class, TEMPORAL_ACCESSOR_NODE_MAPPER_OPTIONS);
        // 处理时间戳格式
        if (options.useTimestamp()) {
            return new LongNode(Instant.from(value).toEpochMilli());
        } else { // 处理字符串格式
            var formatter = options.getFormatter(type);
            return new StringNode(formatter.format(value));
        }
    }

    @Override
    public T nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        var options = context.getMapperOptions(TemporalAccessorNodeMapperOptions.class, TEMPORAL_ACCESSOR_NODE_MAPPER_OPTIONS);
        // 处理时间戳格式
        if (options.useTimestamp()) {
            try {
                return temporalQuery.queryFrom(Instant.ofEpochMilli(node.asLong()));
            } catch (DateTimeParseException | NumberFormatException e) {
                throw new NodeToObjectException(e);
            }
        } else {// 处理字符串格式
            var formatter = options.getFormatter(type);
            try {
                return formatter.parse(node.asString(), temporalQuery);
            } catch (DateTimeParseException e) {
                throw new NodeToObjectException(e);
            }
        }
    }

}
