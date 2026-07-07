package dev.scx.object.x.mapper.record;

import dev.scx.reflect.ConstructorInfo;
import dev.scx.reflect.RecordComponentInfo;

public final class RecordNodeMapperHelper {

    /// 针对某些非 public 的 record, 提前尝试开放其构造函数与 accessor 的反射访问权限.
    ///
    /// 这里只做尝试, 失败时忽略.
    /// 因为有些场景下可能只会走序列化或只会走反序列化,
    /// 没有必要在 mapper 创建阶段因为访问权限问题直接失败.
    /// 真正需要调用时, 再由具体读写流程决定是否抛出异常.
    public static void trySetAccessible(ConstructorInfo recordConstructor, RecordComponentInfo[] recordComponents) {
        recordConstructor.rawConstructor().trySetAccessible();
        for (var recordComponent : recordComponents) {
            recordComponent.rawRecordComponent().getAccessor().trySetAccessible();
        }
    }

}
