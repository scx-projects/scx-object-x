package dev.scx.object.x.mapper.byte_array;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

/// ByteArrayNodeMapperOptions
///
/// @author scx567888
public final class ByteArrayNodeMapperOptions implements TypeNodeMapperOptions {

    private boolean useBase64;

    public ByteArrayNodeMapperOptions() {
        this.useBase64 = true;
    }

    public ByteArrayNodeMapperOptions useBase64(boolean useBase64) {
        this.useBase64 = useBase64;
        return this;
    }

    public boolean useBase64() {
        return useBase64;
    }

}
