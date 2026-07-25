package dev.scx.object.x.mapper.time;

import dev.scx.object.x.mapper.TypeNodeMapperOptions;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static java.time.format.DateTimeFormatter.*;

/// OffsetDateTimeNodeMapperOptions
///
/// @author scx567888
public final class OffsetDateTimeNodeMapperOptions implements TypeNodeMapperOptions {

    private static final DateTimeFormatter DEFAULT_YEAR_FORMATTER = ofPattern("yyyy");
    private static final DateTimeFormatter DEFAULT_MONTH_FORMATTER = ofPattern("MM");
    private static final DateTimeFormatter DEFAULT_MONTH_DAY_FORMATTER = ofPattern("--MM-dd");
    private static final DateTimeFormatter DEFAULT_YEAR_MONTH_FORMATTER = ofPattern("yyyy-MM");
    private static final DateTimeFormatter DEFAULT_DAY_OF_WEEK_FORMATTER = ofPattern("e");


    private DateTimeFormatter LocalDateTime_Formatter;
    private DateTimeFormatter LocalDate_Formatter;
    private DateTimeFormatter LocalTime_Formatter;
    private DateTimeFormatter OffsetDateTime_Formatter;
    private DateTimeFormatter OffsetTime_Formatter;
    private DateTimeFormatter ZonedDateTime_Formatter;
    private DateTimeFormatter Year_Formatter;
    private DateTimeFormatter Month_Formatter;
    private DateTimeFormatter MonthDay_Formatter;
    private DateTimeFormatter YearMonth_Formatter;
    private DateTimeFormatter DayOfWeek_Formatter;
    private DateTimeFormatter Instant_Formatter;

    private boolean useTimestamp;

    public OffsetDateTimeNodeMapperOptions() {
        this.LocalDateTime_Formatter = ISO_LOCAL_DATE_TIME;
        this.LocalDate_Formatter = ISO_LOCAL_DATE;
        this.LocalTime_Formatter = ISO_LOCAL_TIME;
        this.OffsetDateTime_Formatter = ISO_OFFSET_DATE_TIME;
        this.OffsetTime_Formatter = ISO_OFFSET_TIME;
        this.ZonedDateTime_Formatter = ISO_ZONED_DATE_TIME;
        this.Year_Formatter = DEFAULT_YEAR_FORMATTER;
        this.Month_Formatter = DEFAULT_MONTH_FORMATTER;
        this.MonthDay_Formatter = DEFAULT_MONTH_DAY_FORMATTER;
        this.YearMonth_Formatter = DEFAULT_YEAR_MONTH_FORMATTER;
        this.DayOfWeek_Formatter = DEFAULT_DAY_OF_WEEK_FORMATTER;
        this.Instant_Formatter = ISO_INSTANT;
        this.useTimestamp = false;
    }

    public OffsetDateTimeNodeMapperOptions setFormatter(Class<? extends TemporalAccessor> type, DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException("formatter cannot be null");
        }
        if (type == LocalDateTime.class) {
            this.LocalDateTime_Formatter = formatter;
        } else if (type == LocalDate.class) {
            this.LocalDate_Formatter = formatter;
        } else if (type == LocalTime.class) {
            this.LocalTime_Formatter = formatter;
        } else if (type == OffsetDateTime.class) {
            this.OffsetDateTime_Formatter = formatter;
        } else if (type == OffsetTime.class) {
            this.OffsetTime_Formatter = formatter;
        } else if (type == ZonedDateTime.class) {
            this.ZonedDateTime_Formatter = formatter;
        } else if (type == Year.class) {
            this.Year_Formatter = formatter;
        } else if (type == Month.class) {
            this.Month_Formatter = formatter;
        } else if (type == MonthDay.class) {
            this.MonthDay_Formatter = formatter;
        } else if (type == YearMonth.class) {
            this.YearMonth_Formatter = formatter;
        } else if (type == DayOfWeek.class) {
            this.DayOfWeek_Formatter = formatter;
        } else if (type == Instant.class) {
            this.Instant_Formatter = formatter;
        } else {
            throw new IllegalArgumentException("Unsupported temporal type: " + type);
        }
        return this;
    }

    public DateTimeFormatter getFormatter(Class<? extends TemporalAccessor> type) {
        if (type == LocalDateTime.class) {
            return this.LocalDateTime_Formatter;
        } else if (type == LocalDate.class) {
            return this.LocalDate_Formatter;
        } else if (type == LocalTime.class) {
            return this.LocalTime_Formatter;
        } else if (type == OffsetDateTime.class) {
            return this.OffsetDateTime_Formatter;
        } else if (type == OffsetTime.class) {
            return this.OffsetTime_Formatter;
        } else if (type == ZonedDateTime.class) {
            return this.ZonedDateTime_Formatter;
        } else if (type == Year.class) {
            return this.Year_Formatter;
        } else if (type == Month.class) {
            return this.Month_Formatter;
        } else if (type == MonthDay.class) {
            return this.MonthDay_Formatter;
        } else if (type == YearMonth.class) {
            return this.YearMonth_Formatter;
        } else if (type == DayOfWeek.class) {
            return this.DayOfWeek_Formatter;
        } else if (type == Instant.class) {
            return this.Instant_Formatter;
        } else {
            throw new IllegalArgumentException("Unsupported temporal type: " + type);
        }
    }

    public OffsetDateTimeNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
