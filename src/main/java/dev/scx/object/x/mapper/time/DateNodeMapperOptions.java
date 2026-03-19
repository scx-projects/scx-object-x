package dev.scx.object.x.mapper.time;

import dev.scx.object.x.TypeNodeMapperOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/// DateNodeMapperOptions
///
/// @author scx567888
/// @version 0.0.1
public final class DateNodeMapperOptions implements TypeNodeMapperOptions {

    private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat();

    private DateFormat dateFormat;
    private boolean useTimestamp;

    public DateNodeMapperOptions() {
        this.dateFormat = DEFAULT_DATE_FORMAT;
        this.useTimestamp = false;
    }

    public DateNodeMapperOptions dataFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public DateFormat dateFormat() {
        return dateFormat;
    }

    public DateNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
