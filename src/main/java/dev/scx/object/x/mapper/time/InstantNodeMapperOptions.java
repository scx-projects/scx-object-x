package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/// InstantNodeMapperOptions
///
/// @author scx567888
public final class InstantNodeMapperOptions implements TypeNodeMapperOptions {

    private DateTimeFormatter formatter;

    private boolean useTimestamp;

    public InstantNodeMapperOptions() {
        this.formatter = ISO_INSTANT;
        this.useTimestamp = false;
    }

    public InstantNodeMapperOptions formatter(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException("formatter cannot be null");
        }
        this.formatter = formatter;
        return this;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }

    public InstantNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
