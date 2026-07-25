package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

/// MonthDayNodeMapperOptions
///
/// @author scx567888
public final class MonthDayNodeMapperOptions implements TypeNodeMapperOptions {

    private static final DateTimeFormatter DEFAULT_MONTH_DAY_FORMATTER = ofPattern("--MM-dd");

    private DateTimeFormatter formatter;

    private boolean useTimestamp;

    public MonthDayNodeMapperOptions() {
        this.formatter = DEFAULT_MONTH_DAY_FORMATTER;
        this.useTimestamp = false;
    }

    public MonthDayNodeMapperOptions formatter(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException("formatter cannot be null");
        }
        this.formatter = formatter;
        return this;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }

    public MonthDayNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
