package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

/// YearMonthNodeMapperOptions
///
/// @author scx567888
public final class YearMonthNodeMapperOptions implements TypeNodeMapperOptions {

    private static final DateTimeFormatter DEFAULT_YEAR_MONTH_FORMATTER = ofPattern("yyyy-MM");

    private DateTimeFormatter formatter;

    private boolean useTimestamp;

    public YearMonthNodeMapperOptions() {
        this.formatter = DEFAULT_YEAR_MONTH_FORMATTER;
        this.useTimestamp = false;
    }

    public YearMonthNodeMapperOptions formatter(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException("formatter cannot be null");
        }
        this.formatter = formatter;
        return this;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }

    public YearMonthNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
