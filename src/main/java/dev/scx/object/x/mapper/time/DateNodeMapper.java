package dev.scx.object.x.mapper.time;

import dev.scx.node.LongNode;
import dev.scx.node.StringNode;
import dev.scx.node.ValueNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static dev.scx.reflect.ScxReflect.typeOf;

/// DateNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class DateNodeMapper implements TypeNodeMapper<Date, ValueNode> {

    private static final DateNodeMapperOptions DATE_NODE_MAPPER_OPTIONS = new DateNodeMapperOptions();

    @Override
    public TypeInfo valueType() {
        return typeOf(Date.class);
    }

    @Override
    public Class<ValueNode> nodeType() {
        return ValueNode.class;
    }

    @Override
    public ValueNode valueToNode(Date value, ObjectToNodeContext context) {
        var options = context.getMapperOptions(DateNodeMapperOptions.class, DATE_NODE_MAPPER_OPTIONS);
        // 处理时间戳格式
        if (options.useTimestamp()) {
            return new LongNode(value.getTime());
        } else { // 处理字符串格式 注意 DateFormat 线程安全问题
            var dateFormat = (DateFormat) options.dateFormat().clone();
            return new StringNode(dateFormat.format(value));
        }
    }

    @Override
    public Date nodeToValue(ValueNode node, NodeToObjectContext context) throws NodeToObjectException {
        var options = context.getMapperOptions(DateNodeMapperOptions.class, DATE_NODE_MAPPER_OPTIONS);
        // 处理时间戳格式
        if (options.useTimestamp()) {
            try {
                return new Date(node.asLong());
            } catch (NumberFormatException e) {
                throw new NodeToObjectException(e);
            }
        } else { // 处理字符串格式 注意 DateFormat 线程安全问题
            var dateFormat = (DateFormat) options.dateFormat().clone();
            try {
                return dateFormat.parse(node.asString());
            } catch (ParseException e) {
                throw new NodeToObjectException(e);
            }
        }
    }

}
