package dev.scx.object.x.mapper.bean;

import dev.scx.reflect.FieldInfo;

public interface BeanFieldReadPolicy {

    /// 允许返回 null, 表示采用默认策略.
    BeanFieldReadResult apply(FieldInfo fieldInfo);

}
