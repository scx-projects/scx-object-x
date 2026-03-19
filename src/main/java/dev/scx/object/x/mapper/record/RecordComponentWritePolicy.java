package dev.scx.object.x.mapper.record;

import dev.scx.reflect.RecordComponentInfo;

public interface RecordComponentWritePolicy {

    /// 允许返回 null, 表示采用默认策略.
    RecordComponentWriteResult apply(RecordComponentInfo fieldInfo, Object fieldValue);

}
