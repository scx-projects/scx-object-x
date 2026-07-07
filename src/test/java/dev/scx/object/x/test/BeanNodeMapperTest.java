package dev.scx.object.x.test;

import dev.scx.node.Node;
import dev.scx.node.ObjectNode;
import dev.scx.node.StringNode;
import dev.scx.object.x.DefaultObjectNodeConvertConfig;
import dev.scx.object.x.mapper.bean.BeanFieldReadResult;
import dev.scx.object.x.mapper.bean.BeanFieldWriteResult;
import dev.scx.object.x.mapper.bean.BeanNodeMapperOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import static dev.scx.object.x.DefaultObjectNodeConverter.DEFAULT_OBJECT_NODE_CONVERTER;
import static dev.scx.reflect.ScxReflect.typeOf;

public class BeanNodeMapperTest {

    public static void main() {
        test1();
    }

    @Test
    public static void test1() {

        Node node = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode(new Student("scx567888"), DefaultObjectNodeConvertConfig.of().putMapperOptions(
            new BeanNodeMapperOptions().beanFieldWritePolicy((c, v) -> {
                var annotation = c.findAnnotation(NewName.class);
                if (annotation != null) {
                    // 我们也支持修改 value
                    return BeanFieldWriteResult.of(annotation.value(), v + "_1");
                }
                return null;
            })
        ));

        Assert.assertEquals(((ObjectNode) node).get("student_name"), new StringNode("scx567888_1"));

        Student u = DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node, typeOf(Student.class), DefaultObjectNodeConvertConfig.of().putMapperOptions(
            new BeanNodeMapperOptions().beanFieldReadPolicy((c) -> {
                var annotation = c.findAnnotation(NewName.class);
                if (annotation != null) {
                    return BeanFieldReadResult.of(annotation.value());
                }
                return null;
            })
        ));

        Assert.assertEquals(u.name, "scx567888_1");
    }

}
