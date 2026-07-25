package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_TIME;

/// OffsetTimeNodeMapperOptions
///
/// @author scx567888
public final class OffsetTimeNodeMapperOptions implements TypeNodeMapperOptions {

    private DateTimeFormatter formatter;

    private boolean useTimestamp;

    public OffsetTimeNodeMapperOptions() {
        this.formatter = ISO_OFFSET_TIME;
        this.useTimestamp = false;
    }

    public OffsetTimeNodeMapperOptions formatter(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException("formatter cannot be null");
        }
        this.formatter = formatter;
        return this;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }

    public OffsetTimeNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
