package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

/// DayOfWeekNodeMapperOptions
///
/// @author scx567888
public final class DayOfWeekNodeMapperOptions implements TypeNodeMapperOptions {

    private static final DateTimeFormatter DEFAULT_DAY_OF_WEEK_FORMATTER = ofPattern("e");

    private DateTimeFormatter formatter;

    private boolean useTimestamp;

    public DayOfWeekNodeMapperOptions() {
        this.formatter = DEFAULT_DAY_OF_WEEK_FORMATTER;
        this.useTimestamp = false;
    }

    public DayOfWeekNodeMapperOptions formatter(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException("formatter cannot be null");
        }
        this.formatter = formatter;
        return this;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }

    public DayOfWeekNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
