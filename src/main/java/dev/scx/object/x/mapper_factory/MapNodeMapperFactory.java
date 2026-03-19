package dev.scx.object.x.mapper_factory;

import dev.scx.object.x.TypeNodeMapperFactory;
import dev.scx.object.x.mapper.map.MapNodeMapper;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.util.Map;

import static dev.scx.reflect.ScxReflect.typeOf;

/// MapNodeMapperFactory
///
/// @author scx567888
/// @version 0.0.1
public final class MapNodeMapperFactory implements TypeNodeMapperFactory {

    @Override
    public MapNodeMapper createMapper(TypeInfo typeInfo) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (Map.class.isAssignableFrom(typeInfo.rawClass())) {
                var mapType = classInfo.findSuperType(Map.class);
                if (mapType == null) {
                    // 理论上不可能发生
                    throw new IllegalStateException("Map class not found");
                }
                var keyType = mapType.bindings().get("K");
                // 为 null 回退到 String
                if (keyType == null) {
                    keyType = typeOf(String.class);
                }
                // 为 null 回退到 Object
                var valueType = mapType.bindings().get("V");
                if (valueType == null) {
                    valueType = typeOf(Object.class);
                }
                // 这个 keyType 和 valueType 实际上只能用于 nodeToValue,
                // 因为在 valueToNode 的时候, 由于 Map 的泛型是被擦除的,
                // 所以我们是不能假定 每一个元素的类型都真的是 keyType, valueType
                return new MapNodeMapper(classInfo, keyType, valueType);
            }
        }
        return null;
    }

}
