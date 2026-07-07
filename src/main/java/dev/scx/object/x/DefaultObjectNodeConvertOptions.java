package dev.scx.object.x;

import dev.scx.object.ObjectNodeConvertOptions;
import dev.scx.object.x.adapter.NodeTypeAdapter;
import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.util.Map;

/// DefaultObjectNodeConvertOptions
///
/// @author scx567888
public interface DefaultObjectNodeConvertOptions extends ObjectNodeConvertOptions {

    int maxNestingDepth();

    Map<Class<? extends TypeNodeMapperOptions>, TypeNodeMapperOptions> mapperOptionsMap();

    NodeTypeAdapter nodeTypeAdapter();

}
