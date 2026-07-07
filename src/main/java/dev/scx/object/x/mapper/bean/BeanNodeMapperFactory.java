package dev.scx.object.x.mapper.bean;

import dev.scx.object.x.mapper.TypeNodeMapperFactory;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ClassKind;
import dev.scx.reflect.TypeInfo;

/// BeanNodeMapperFactory
///
/// @author scx567888
public final class BeanNodeMapperFactory implements TypeNodeMapperFactory {

    @Override
    public BeanNodeMapper createMapper(TypeInfo typeInfo) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (classInfo.classKind() == ClassKind.CLASS) {
                return new BeanNodeMapper(classInfo);
            }
        }
        return null;
    }

}
