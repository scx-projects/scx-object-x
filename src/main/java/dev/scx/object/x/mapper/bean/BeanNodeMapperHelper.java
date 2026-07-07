package dev.scx.object.x.mapper.bean;

import dev.scx.reflect.AccessModifier;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ConstructorInfo;
import dev.scx.reflect.FieldInfo;

import java.util.Arrays;

public final class BeanNodeMapperHelper {

    /// 针对某些非 public 的 class, 提前尝试开放其构造函数 和 字段 的反射访问权限.
    ///
    /// 这里只做尝试, 失败时忽略.
    /// 因为有些场景下可能只会走序列化或只会走反序列化,
    /// 没有必要在 mapper 创建阶段因为访问权限问题直接失败.
    /// 真正需要调用时, 再由具体读写流程决定是否抛出异常.
    public static void trySetAccessible(ConstructorInfo constructor, FieldInfo[] fields) {
        // constructor 可能为 null 这里跳过
        if (constructor != null) {
            constructor.rawConstructor().trySetAccessible();
        }
        for (var field : fields) {
            field.rawField().trySetAccessible();
        }
    }

    /// 可读的字段, 这里只要 public 的实例字段
    public static FieldInfo[] filterReadableFields(ClassInfo classInfo) {
        // 因为 BeanNodeMapper 每种类型的对象只会创建一次, 所以这里 使用 Stream 并没有什么性能问题
        // 注意我们这里需要连父级的字段也带上
        return Arrays.stream(classInfo.allFields())
            .filter(c -> !c.isStatic() && c.accessModifier() == AccessModifier.PUBLIC)
            .toArray(FieldInfo[]::new);
    }

    /// 可写的字段, 相较于可读 我们过滤掉 final
    public static FieldInfo[] filterWritableFields(FieldInfo[] readableFields) {
        // 因为 BeanNodeMapper 每种类型的对象只会创建一次, 所以这里 使用 Stream 并没有什么性能问题
        return Arrays.stream(readableFields).filter(c -> !c.isFinal()).toArray(FieldInfo[]::new);
    }

}
