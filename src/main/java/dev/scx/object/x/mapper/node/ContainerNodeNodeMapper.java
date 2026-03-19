package dev.scx.object.x.mapper.node;

import dev.scx.node.ContainerNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.NodeToObjectContext;
import dev.scx.object.x.ObjectToNodeContext;
import dev.scx.object.x.TypeNodeMapper;
import dev.scx.reflect.TypeInfo;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ContainerNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class ContainerNodeNodeMapper implements TypeNodeMapper<ContainerNode, ContainerNode> {

    @Override
    public TypeInfo valueType() {
        return typeOf(ContainerNode.class);
    }

    @Override
    public Class<ContainerNode> nodeType() {
        return ContainerNode.class;
    }

    @Override
    public ContainerNode valueToNode(ContainerNode value, ObjectToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public ContainerNode nodeToValue(ContainerNode node, NodeToObjectContext context) throws NodeToObjectException {
        return node.deepCopy();
    }

}
