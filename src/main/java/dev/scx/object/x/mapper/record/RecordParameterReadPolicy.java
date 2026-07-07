package dev.scx.object.x.mapper.record;

import dev.scx.reflect.ParameterInfo;

public interface RecordParameterReadPolicy {

    /// 允许返回 null, 表示采用默认策略.
    RecordParameterReadResult apply(ParameterInfo parameterInfo);

}
