package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

/// MonthNodeMapperOptions
///
/// @author scx567888
public final class MonthNodeMapperOptions implements TypeNodeMapperOptions {

    private static final DateTimeFormatter DEFAULT_MONTH_FORMATTER = ofPattern("MM");

    private DateTimeFormatter formatter;

    private boolean useTimestamp;

    public MonthNodeMapperOptions() {
        this.formatter = DEFAULT_MONTH_FORMATTER;
        this.useTimestamp = false;
    }

    public MonthNodeMapperOptions formatter(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException("formatter cannot be null");
        }
        this.formatter = formatter;
        return this;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }

    public MonthNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
