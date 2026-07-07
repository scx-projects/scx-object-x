package dev.scx.object.x.test;

import dev.scx.object.NodeToObjectException;
import dev.scx.object.x.DefaultObjectNodeConvertConfig;
import dev.scx.object.x.adapter.CompositeNodeTypeAdapter;
import org.testng.Assert;
import org.testng.annotations.Test;

import static dev.scx.object.x.DefaultObjectNodeConverter.DEFAULT_OBJECT_NODE_CONVERTER;
import static dev.scx.object.x.adapter.SingleElementArrayUnwrapAdapter.SINGLE_ELEMENT_ARRAY_UNWRAP_ADAPTER;
import static dev.scx.object.x.adapter.SingleValueWrapArrayAdapter.SINGLE_VALUE_WRAP_ARRAY_ADAPTER;

public class NodeTypeAdapterTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var node1 = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode(new int[]{1}, DefaultObjectNodeConvertConfig.of());
        var node2 = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode(new int[]{1, 2}, DefaultObjectNodeConvertConfig.of());
        var node3 = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode(1, DefaultObjectNodeConvertConfig.of());
        var node4 = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode("abc", DefaultObjectNodeConvertConfig.of());

        // 直接转换
        var o1 = DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node1, int[].class, DefaultObjectNodeConvertConfig.of());
        Assert.assertEquals(o1, new int[]{1});

        // 转换失败
        Assert.assertThrows(NodeToObjectException.class, () -> {
            var o2 = DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node1, int.class, DefaultObjectNodeConvertConfig.of());
        });

        // 添加 SINGLE_ELEMENT_ARRAY_UNWRAP_ADAPTER 转换成功
        var o2 = DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node1, int.class, DefaultObjectNodeConvertConfig.of().nodeTypeAdapter(new CompositeNodeTypeAdapter(SINGLE_ELEMENT_ARRAY_UNWRAP_ADAPTER, SINGLE_VALUE_WRAP_ARRAY_ADAPTER)));
        Assert.assertEquals(o2, 1);

        // 转换失败 因为存在多个值.
        Assert.assertThrows(NodeToObjectException.class, () -> {
            var o3 = DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node2, int.class, DefaultObjectNodeConvertConfig.of().nodeTypeAdapter(new CompositeNodeTypeAdapter(SINGLE_VALUE_WRAP_ARRAY_ADAPTER, SINGLE_ELEMENT_ARRAY_UNWRAP_ADAPTER)));
        });

        // 直接转换
        var o4 = DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node3, int.class, DefaultObjectNodeConvertConfig.of());

        // 转换失败 因为存在多个值.
        Assert.assertThrows(NodeToObjectException.class, () -> {
            var o5 = DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node3, int[].class, DefaultObjectNodeConvertConfig.of());
        });

        // 添加 SINGLE_VALUE_WRAP_ARRAY_ADAPTER 转换成功.
        var o6 = DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node3, int[].class, DefaultObjectNodeConvertConfig.of().nodeTypeAdapter(new CompositeNodeTypeAdapter(SINGLE_VALUE_WRAP_ARRAY_ADAPTER, SINGLE_ELEMENT_ARRAY_UNWRAP_ADAPTER)));
        Assert.assertEquals(o6, new int[]{1});

        // 转换失败 即使添加 SINGLE_VALUE_WRAP_ARRAY_ADAPTER. 因为 数值本身无法转换.
        Assert.assertThrows(NodeToObjectException.class, () -> {
            var o7 = DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node4, int[].class, DefaultObjectNodeConvertConfig.of().nodeTypeAdapter(new CompositeNodeTypeAdapter(SINGLE_ELEMENT_ARRAY_UNWRAP_ADAPTER, SINGLE_VALUE_WRAP_ARRAY_ADAPTER)));
        });

    }


}
