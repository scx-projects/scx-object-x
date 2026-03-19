package dev.scx.object.x.mapper_factory;

import dev.scx.object.x.TypeNodeMapperFactory;
import dev.scx.object.x.mapper.record.RecordNodeMapper;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ClassKind;
import dev.scx.reflect.TypeInfo;

/// RecordNodeMapperFactory
///
/// @author scx567888
/// @version 0.0.1
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
