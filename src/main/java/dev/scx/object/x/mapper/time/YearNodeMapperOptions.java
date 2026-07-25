package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

/// YearNodeMapperOptions
///
/// @author scx567888
public final class YearNodeMapperOptions implements TypeNodeMapperOptions {

    private static final DateTimeFormatter DEFAULT_YEAR_FORMATTER = ofPattern("yyyy");

    private DateTimeFormatter formatter;

    private boolean useTimestamp;

    public YearNodeMapperOptions() {
        this.formatter = DEFAULT_YEAR_FORMATTER;
        this.useTimestamp = false;
    }

    public YearNodeMapperOptions formatter(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException("formatter cannot be null");
        }
        this.formatter = formatter;
        return this;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }

    public YearNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
