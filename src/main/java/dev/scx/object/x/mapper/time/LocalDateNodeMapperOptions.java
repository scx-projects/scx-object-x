package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/// LocalDateNodeMapperOptions
///
/// @author scx567888
public final class LocalDateNodeMapperOptions implements TypeNodeMapperOptions {

    private DateTimeFormatter formatter;

    private boolean useTimestamp;

    public LocalDateNodeMapperOptions() {
        this.formatter = ISO_LOCAL_DATE;
        this.useTimestamp = false;
    }

    public LocalDateNodeMapperOptions formatter(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException("formatter cannot be null");
        }
        this.formatter = formatter;
        return this;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }

    public LocalDateNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
