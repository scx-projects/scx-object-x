package dev.scx.object.x.mapper.map;

import dev.scx.object.x.TypeNodeMapperOptions;

public final class MapNodeMapperOptions implements TypeNodeMapperOptions {

    private String nullKey;
    private boolean ignoreNullValue;

    public MapNodeMapperOptions() {
        this.nullKey = "";
        this.ignoreNullValue = false;
    }

    /// key 为 null 时 对应的 key
    public String nullKey() {
        return nullKey;
    }

    public MapNodeMapperOptions nullKey(String nullKey) {
        if (nullKey == null) {
            throw new NullPointerException("nullKey cannot be null");
        }
        this.nullKey = nullKey;
        return this;
    }

    /// 是否忽略 null 值
    public boolean ignoreNullValue() {
        return ignoreNullValue;
    }

    public MapNodeMapperOptions ignoreNullValue(boolean ignoreNullValue) {
        this.ignoreNullValue = ignoreNullValue;
        return this;
    }

}
