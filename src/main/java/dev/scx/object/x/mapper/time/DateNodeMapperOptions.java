package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/// DateNodeMapperOptions
///
/// @author scx567888
public final class DateNodeMapperOptions implements TypeNodeMapperOptions {

    private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat();

    private DateFormat dateFormat;
    private boolean useTimestamp;

    public DateNodeMapperOptions() {
        this.dateFormat = DEFAULT_DATE_FORMAT;
        this.useTimestamp = false;
    }

    public DateNodeMapperOptions dateFormat(DateFormat dateFormat) {
        if (dateFormat == null) {
            throw new NullPointerException("dateFormat cannot be null");
        }
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
