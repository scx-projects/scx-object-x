package dev.scx.object.x;

import dev.scx.object.ObjectNodeConvertOptions;

import java.util.HashMap;
import java.util.Map;

public final class DefaultObjectNodeConvertOptions implements ObjectNodeConvertOptions {

    /// mapperOptionsMap
    private Map<Class<? extends TypeNodeMapperOptions>, TypeNodeMapperOptions> mapperOptionsMap;
    /// 最大嵌套深度
    private int maxNestingDepth;

    public DefaultObjectNodeConvertOptions() {
        this.mapperOptionsMap = null;
        this.maxNestingDepth = 200;
    }

    public int maxNestingDepth() {
        return maxNestingDepth;
    }

    public DefaultObjectNodeConvertOptions maxNestingDepth(int maxNestingDepth) {
        if (maxNestingDepth < 0) {
            throw new IllegalArgumentException("maxNestingDepth cannot < 0");
        }
        this.maxNestingDepth = maxNestingDepth;
        return this;
    }

    public DefaultObjectNodeConvertOptions addMapperOptions(TypeNodeMapperOptions... optionsList) {
        if (mapperOptionsMap == null) {
            mapperOptionsMap = new HashMap<>();
        }
        for (var o : optionsList) {
            mapperOptionsMap.put(o.getClass(), o);
        }
        return this;
    }

    public Map<Class<? extends TypeNodeMapperOptions>, TypeNodeMapperOptions> mapperOptionsMap() {
        return mapperOptionsMap;
    }

}
