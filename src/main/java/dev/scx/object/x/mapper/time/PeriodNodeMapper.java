package dev.scx.object.x.mapper.time;

import dev.scx.node.StringNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.context.NodeToObjectContext;
import dev.scx.object.x.context.ObjectToNodeContext;
import dev.scx.object.x.mapper.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import java.time.DateTimeException;
import java.time.Period;
import java.util.function.Function;

import static dev.scx.reflect.ScxReflect.typeOf;

/// PeriodNodeMapper
///
/// @author scx567888
public final class PeriodNodeMapper implements TypeNodeMapper<Period, StringNode> {

    public PeriodNodeMapper() {

    }

    @Override
    public TypeInfo valueType() {
        return typeOf(Period.class);
    }

    @Override
    public Class<StringNode> nodeType(NodeToObjectContext context) {
        return StringNode.class;
    }

    @Override
    public StringNode valueToNode(Period value, ObjectToNodeContext context) {
        return new StringNode(value.toString());
    }

    @Override
    public Period nodeToValue(StringNode node, NodeToObjectContext context) throws NodeToObjectException {
        try {
            return Period.parse(node.asString());
        } catch (DateTimeException e) {
            throw new NodeToObjectException(e);
        }
    }

}
