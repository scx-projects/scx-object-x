package dev.scx.object.x;

import dev.scx.object.x.adapter.NodeTypeAdapter;
import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.util.HashMap;
import java.util.Map;

/// DefaultObjectNodeConvertConfig (本质上是 具备 setter 的 DefaultObjectNodeConvertOptions)
///
/// @author scx567888
public final class DefaultObjectNodeConvertConfig implements DefaultObjectNodeConvertOptions {

    /// 最大嵌套深度
    private int maxNestingDepth;
    /// mapperOptionsMap
    private Map<Class<? extends TypeNodeMapperOptions>, TypeNodeMapperOptions> mapperOptionsMap;
    /// node 类型适配器
    private NodeTypeAdapter nodeTypeAdapter;

    private DefaultObjectNodeConvertConfig() {
        this.maxNestingDepth = 200;
        this.mapperOptionsMap = null;
        this.nodeTypeAdapter = null;
    }

    public static DefaultObjectNodeConvertConfig of() {
        return new DefaultObjectNodeConvertConfig();
    }

    public static DefaultObjectNodeConvertConfig copyOf(DefaultObjectNodeConvertOptions options) {
        var config = new DefaultObjectNodeConvertConfig();
        config.maxNestingDepth(options.maxNestingDepth());
        config.mapperOptionsMap(options.mapperOptionsMap());
        config.nodeTypeAdapter(options.nodeTypeAdapter());
        return config;
    }

    @Override
    public int maxNestingDepth() {
        return maxNestingDepth;
    }

    public DefaultObjectNodeConvertConfig maxNestingDepth(int maxNestingDepth) {
        if (maxNestingDepth < 0) {
            throw new IllegalArgumentException("maxNestingDepth cannot < 0");
        }
        this.maxNestingDepth = maxNestingDepth;
        return this;
    }

    @Override
    public Map<Class<? extends TypeNodeMapperOptions>, TypeNodeMapperOptions> mapperOptionsMap() {
        return mapperOptionsMap;
    }

    public DefaultObjectNodeConvertConfig mapperOptionsMap(Map<Class<? extends TypeNodeMapperOptions>, TypeNodeMapperOptions> mapperOptionsMap) {
        if (mapperOptionsMap == null) {
            this.mapperOptionsMap = null;
        } else {
            this.mapperOptionsMap = new HashMap<>(mapperOptionsMap);
        }
        return this;
    }

    @Override
    public NodeTypeAdapter nodeTypeAdapter() {
        return nodeTypeAdapter;
    }

    public DefaultObjectNodeConvertConfig nodeTypeAdapter(NodeTypeAdapter nodeTypeAdapter) {
        this.nodeTypeAdapter = nodeTypeAdapter;
        return this;
    }

    public DefaultObjectNodeConvertConfig putMapperOptions(TypeNodeMapperOptions... optionsList) {
        if (mapperOptionsMap == null) {
            mapperOptionsMap = new HashMap<>();
        }
        for (var o : optionsList) {
            mapperOptionsMap.put(o.getClass(), o);
        }
        return this;
    }

}
