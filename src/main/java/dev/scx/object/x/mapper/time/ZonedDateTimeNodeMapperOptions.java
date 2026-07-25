package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

/// ZonedDateTimeNodeMapperOptions
///
/// @author scx567888
public final class ZonedDateTimeNodeMapperOptions implements TypeNodeMapperOptions {

    private DateTimeFormatter formatter;

    private boolean useTimestamp;

    public ZonedDateTimeNodeMapperOptions() {
        this.formatter = ISO_ZONED_DATE_TIME;
        this.useTimestamp = false;
    }

    public ZonedDateTimeNodeMapperOptions formatter(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException("formatter cannot be null");
        }
        this.formatter = formatter;
        return this;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }

    public ZonedDateTimeNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
