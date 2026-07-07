package dev.scx.object.x.test;

import dev.scx.node.ArrayNode;
import dev.scx.node.Node;
import dev.scx.node.ObjectNode;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.DefaultObjectNodeConvertConfig;
import dev.scx.object.x.DefaultObjectNodeConvertOptions;
import dev.scx.object.x.mapper.bean.BeanFieldReadResult;
import dev.scx.object.x.mapper.bean.BeanFieldWriteResult;
import dev.scx.object.x.mapper.bean.BeanNodeMapperOptions;
import dev.scx.object.x.mapper.primitive.PrimitiveNullPolicy;
import dev.scx.object.x.mapper.time.DateNodeMapperOptions;
import dev.scx.object.x.mapper.time.TemporalAccessorNodeMapperOptions;
import dev.scx.reflect.TypeInfo;
import dev.scx.reflect.TypeReference;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.*;

import static dev.scx.object.x.DefaultObjectNodeConverter.DEFAULT_OBJECT_NODE_CONVERTER;
import static dev.scx.reflect.ScxReflect.typeOf;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ObjectNodeConverterTest {

    public static void main(String[] args) throws ObjectToNodeException, NodeToObjectException {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
        test8();
    }

    public static <T> T convertValue(Object value, TypeInfo type, DefaultObjectNodeConvertOptions options) throws ObjectToNodeException, NodeToObjectException {
        var node = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode(value, options);
        return DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node, type, options);
    }

    public static <T> T convertValue(Object value, Class<T> type, DefaultObjectNodeConvertOptions options) throws ObjectToNodeException, NodeToObjectException {
        var node = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode(value, options);
        return DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node, type, options);
    }

    public static <T> T convertValue(Object value, TypeInfo type) throws ObjectToNodeException, NodeToObjectException {
        var node = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode(value, DefaultObjectNodeConvertConfig.of());
        return DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node, type, DefaultObjectNodeConvertConfig.of());
    }

    public static <T> T convertValue(Object value, Class<T> type) throws ObjectToNodeException, NodeToObjectException {
        var node = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode(value, DefaultObjectNodeConvertConfig.of());
        return DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node, type, DefaultObjectNodeConvertConfig.of());
    }

    public static <T> T convertValue(Object value, TypeReference<T> type) throws ObjectToNodeException, NodeToObjectException {
        return convertValue(value, typeOf(type));
    }

    @Test
    public static void test1() throws ObjectToNodeException, NodeToObjectException {

        var a = convertValue("123", int.class);
        var b = convertValue("123", long.class);
        var c = convertValue("123", Integer.class);
        var d = convertValue("123", new TypeReference<Byte>() {});
        var e = convertValue(null, int.class, DefaultObjectNodeConvertConfig.of().putMapperOptions(PrimitiveNullPolicy.DEFAULT_VALUE));
        Assert.assertThrows(NodeToObjectException.class, () -> {
            var f = convertValue(null, int.class);
        });

        Assert.assertEquals(a, 123);
        Assert.assertEquals(b, 123);
        Assert.assertEquals(c, 123);
        Assert.assertEquals(d, (byte) 123);
        Assert.assertEquals(e, 0);
    }

    @Test
    public static void test2() throws ObjectToNodeException, NodeToObjectException {
        // 准备数据
        var smallList = new ArrayList<Integer>();
        for (int i = 0; i < 20; i = i + 1) {
            smallList.add(i);
        }
        var bigList = new ArrayList<Integer>();
        for (int i = 0; i < 99999; i = i + 1) {
            bigList.add(i);
        }
        var smallArray = smallList.toArray(new Integer[0]);
        var bigArray = bigList.toArray(new Integer[0]);

        var smallIntArray1 = smallList.stream().mapToInt(c -> c).toArray();
        var bitIntArray2 = bigList.stream().mapToInt(c -> c).toArray();


        for (int i = 0; i < 9; i = i + 1) {
            test1_0(smallList, bigList, smallArray, bigArray, smallIntArray1, bitIntArray2);
        }

    }

    public static void test1_0(ArrayList<Integer> smallList, ArrayList<Integer> bigList, Integer[] smallArray, Integer[] bigArray, int[] smallIntArray1, int[] bitIntArray2) throws ObjectToNodeException, NodeToObjectException {

        convertValue(smallList, Integer[].class);
        convertValue(smallList, int[].class);
        convertValue(bigList, Integer[].class);
        convertValue(bigList, int[].class);
        convertValue(smallArray, Integer[].class);
        convertValue(bigArray, int[].class);
        convertValue(smallArray, Integer[].class);
        convertValue(bigArray, int[].class);
        convertValue(smallList, String[].class);
        convertValue(smallList, long[].class);
        convertValue(bigList, String[].class);
        convertValue(bigList, long[].class);
        convertValue(smallArray, String[].class);
        convertValue(bigArray, long[].class);
        convertValue(smallArray, String[].class);
        convertValue(bigArray, long[].class);

        convertValue(smallList, new TypeReference<List<Integer>>() {});
        convertValue(smallList, new TypeReference<List<String>>() {});
        convertValue(bigList, new TypeReference<List<Integer>>() {});
        convertValue(bigList, new TypeReference<List<String>>() {});
        convertValue(smallArray, List.class);
        convertValue(bigArray, List.class);
        convertValue(smallArray, Object.class);
        convertValue(bigArray, Object.class);

    }

    @Test
    public static void test3() throws ObjectToNodeException, NodeToObjectException {
        var user = new User();
        user.name = "小明";
        user.name1 = "小明";
        user.age = 18;
        user.parent = null;
        user.ids = new Object[]{1, 2, 3, 4, 5, 6, 7, 8, 9, "sss"};

        var userList = new ArrayList<User>();
        for (int i = 0; i < 999; i = i + 1) {
            userList.add(user);
        }
        Node node = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode(userList, DefaultObjectNodeConvertConfig.of().putMapperOptions(
            new BeanNodeMapperOptions()
                .beanFieldWritePolicy((fieldInfo, value) -> {
                    // 忽略 null
                    if (value == null) {
                        return BeanFieldWriteResult.ofSkip();
                    }
                    // 忽略 带注解的.
                    if (fieldInfo.findAnnotation(JsonIgnore.class) != null) {
                        return BeanFieldWriteResult.ofSkip();
                    }
                    return null;
                })
        ));
        for (var n : (ArrayNode) node) {
            ((ObjectNode) n).put("name1", ((ObjectNode) n).get("name"));
        }

        User[] o = DEFAULT_OBJECT_NODE_CONVERTER.nodeToObject(node, typeOf(new TypeReference<User[]>() {}), DefaultObjectNodeConvertConfig.of().putMapperOptions(
            new BeanNodeMapperOptions()
                .beanFieldReadPolicy((fieldInfo) -> {
                    // 忽略 带注解的.
                    if (fieldInfo.findAnnotation(JsonIgnore.class) != null) {
                        return BeanFieldReadResult.ofSkip();
                    }
                    return null;
                })
        ));

        System.out.println(o.length);

    }

    @Test
    public static void test4() throws ObjectToNodeException, NodeToObjectException {
        var map = new HashMap<>();
        for (int i = 0; i < 999; i = i + 1) {
            map.put(i, i);
        }
        map.put(map, map);
        Assert.assertThrows(ObjectToNodeException.class, () -> {
            Node node = DEFAULT_OBJECT_NODE_CONVERTER.objectToNode(map, DefaultObjectNodeConvertConfig.of());
        });
    }


    @Test
    public static void test5() throws ObjectToNodeException, NodeToObjectException {

        var now = OffsetDateTime.now();
        for (int i = 0; i < 99999; i = i + 1) {

            var l = convertValue(now, Date.class, DefaultObjectNodeConvertConfig.of()
                .putMapperOptions(new TemporalAccessorNodeMapperOptions().useTimestamp(true))
                .putMapperOptions(new DateNodeMapperOptions().useTimestamp(true)));

//            var l2 = OBJECT_MAPPER.convertValue(now, Date.class);

        }

    }

    @Test
    public static void test6() {
        // 测试枚举的 匿名子类.
        String s = convertValue(AAA.C, String.class);
        AAA aaa = convertValue(s, AAA.class);
        AAA bbb = convertValue("B", AAA.C.getClass());
        Assert.assertEquals(s, "C");
        Assert.assertEquals(aaa, AAA.C);
        Assert.assertEquals(bbb, AAA.B);
    }

    @Test
    public static void test7() {
        var str = "x\\a\\b\\c\\d.txt";
        var path = convertValue(str, Path.class);
        var file = convertValue(path, File.class);
        var path1 = convertValue(file, Path.class);
        var str1 = convertValue(path1, String.class);

        Assert.assertEquals(str1, str);
        Assert.assertEquals(path, path1);
    }

    @Test
    public static void test8() {
        var str = convertValue(UTF_8, String.class);
        var charset = convertValue(str, Charset.class);

        Assert.assertEquals(charset, UTF_8);
    }

    public enum AAA {
        A,
        B,
        C {
            @Override
            public String toString() {
                return "CCC";
            }
        }
    }

    public static class User {
        public String name;
        @JsonIgnore
        public String name1;
        public Integer age;
        public User parent;
        public Object[] ids;
        public Map<String, Integer> map;
    }

}
