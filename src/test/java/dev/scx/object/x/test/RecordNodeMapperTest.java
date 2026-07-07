package dev.scx.object.x.test;

import dev.scx.node.Node;
import dev.scx.node.ObjectNode;
import dev.scx.node.StringNode;
import dev.scx.object.x.DefaultObjectNodeConvertConfig;
import dev.scx.object.x.mapper.record.RecordComponentWriteResult;
import dev.scx.object.x.mapper.record.RecordNodeMapperOptions;
import dev.scx.object.x.mapper.record.RecordParameterReadResult;
import org.testng.Assert;
import org.testng.annotations.Test;

import static dev.scx.object.x.DefaultObjectNodeConverter.DEFAULT_OBJECT_NODE_CONVERTER;
import static dev.scx.reflect.ScxReflect.typeOf;

public class RecordNodeMapperTest {

    public static void main() {
        test1();
    }

    @Test
    public static void test1() {
        Node node = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode(new User("scx567888"), DefaultObjectNodeConvertConfig.of().putMapperOptions(
            new RecordNodeMapperOptions().recordComponentWritePolicy((c, v) -> {
                var annotation = c.findAnnotation(NewName.class);
                if (annotation != null) {
                    return RecordComponentWriteResult.of(annotation.value(), v);
                }
                return null;
            })
        ));

        Assert.assertEquals(((ObjectNode) node).get("NAME"), new StringNode("scx567888"));

        User u = DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node, typeOf(User.class), DefaultObjectNodeConvertConfig.of().putMapperOptions(
            new RecordNodeMapperOptions().recordParameterReadPolicy((c) -> {
                var annotation = c.findAnnotation(NewName.class);
                if (annotation != null) {
                    return RecordParameterReadResult.of(annotation.value());
                }
                return null;
            })
        ));

        Assert.assertEquals(u.name(), "scx567888");
    }

}
