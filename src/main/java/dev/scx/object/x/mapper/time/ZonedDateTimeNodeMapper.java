package dev.scx.object.x.mapper.time;

import dev.scx.node.LongNode;
import dev.scx.node.StringNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalQuery;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ZonedDateTimeNodeMapper
///
/// @author scx567888
public final class ZonedDateTimeNodeMapper implements TypeNodeMapper<ZonedDateTime, ValueNode> {

    private static final ZonedDateTimeNodeMapperOptions DEFAULT_OPTIONS = new ZonedDateTimeNodeMapperOptions();

    private final TemporalQuery<ZonedDateTime> temporalQuery;

    public ZonedDateTimeNodeMapper() {
        this.temporalQuery = ZonedDateTime::from;
    }

    @Override
    public TypeInfo valueType() {
        return typeOf(ZonedDateTime.class);
    }

    @Override
    public Class<ValueNode> nodeType(NodeToObjectContext context) {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(ZonedDateTime value, ObjectToNodeContext context) throws ObjectToNodeException {
        var options = context.getMapperOptions(ZonedDateTimeNodeMapperOptions.class, DEFAULT_OPTIONS);
        // 处理时间戳格式
        if (options.useTimestamp()) {
            try {
                return new LongNode(Instant.from(value).toEpochMilli());
            } catch (DateTimeException | ArithmeticException e) {
                throw new ObjectToNodeException(e);
            }
        } else { // 处理字符串格式
            var formatter = options.formatter();
            try {
                return new StringNode(formatter.format(value));
            } catch (DateTimeException e) {
                throw new ObjectToNodeException(e);
            }
        }
    }

    @Override
    public ZonedDateTime nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        var options = context.getMapperOptions(ZonedDateTimeNodeMapperOptions.class, DEFAULT_OPTIONS);
        // 处理时间戳格式
        if (options.useTimestamp()) {
            try {
                // 这里我们永远使用 精确转换
                return temporalQuery.queryFrom(Instant.ofEpochMilli(node.asLongExact()));
            } catch (DateTimeException | NumberFormatException | ArithmeticException e) {
                throw new NodeToObjectException(e);
            }
        } else {// 处理字符串格式
            var formatter = options.formatter();
            try {
                return formatter.parse(node.asString(), temporalQuery);
            } catch (DateTimeException e) {
                throw new NodeToObjectException(e);
            }
        }
    }

}
