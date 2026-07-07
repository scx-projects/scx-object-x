package dev.scx.object.x.mapper.record;

import dev.scx.object.x.mapper.TypeNodeMapperFactory;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ClassKind;
import dev.scx.reflect.TypeInfo;

/// RecordNodeMapperFactory
///
/// @author scx567888
public final class RecordNodeMapperFactory implements TypeNodeMapperFactory {

    @Override
    public RecordNodeMapper createMapper(TypeInfo typeInfo) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (classInfo.classKind() == ClassKind.RECORD) {
                return new RecordNodeMapper(classInfo);
            }
        }
        return null;
    }

}
