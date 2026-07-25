package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

/// LocalTimeNodeMapperOptions
///
/// @author scx567888
public final class LocalTimeNodeMapperOptions implements TypeNodeMapperOptions {

    private DateTimeFormatter formatter;

    private boolean useTimestamp;

    public LocalTimeNodeMapperOptions() {
        this.formatter = ISO_LOCAL_TIME;
        this.useTimestamp = false;
    }

    public LocalTimeNodeMapperOptions formatter(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException("formatter cannot be null");
        }
        this.formatter = formatter;
        return this;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }

    public LocalTimeNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
