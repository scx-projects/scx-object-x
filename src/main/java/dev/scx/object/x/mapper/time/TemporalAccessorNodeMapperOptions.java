package dev.scx.object.x.mapper.time;

import dev.scx.object.x.TypeNodeMapperOptions;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static java.time.format.DateTimeFormatter.*;

/// TemporalAccessorNodeMapperOptions
///
/// @author scx567888
/// @version 0.0.1
public final class TemporalAccessorNodeMapperOptions implements TypeNodeMapperOptions {

    private static final DateTimeFormatter DEFAULT_YEAR_FORMATER = ofPattern("yyyy");
    private static final DateTimeFormatter DEFAULT_MONTH_FORMATER = ofPattern("MM");
    private static final DateTimeFormatter DEFAULT_MONTH_DAY_FORMATER = ofPattern("--MM-dd");
    private static final DateTimeFormatter DEFAULT_YEAR_MONTH_FORMATER = ofPattern("yyyy-MM");
    private static final DateTimeFormatter DEFAULT_DAY_OF_WEEK_FORMATER = ofPattern("e");


    private DateTimeFormatter LocalDateTime_Formater;
    private DateTimeFormatter LocalDate_Formater;
    private DateTimeFormatter LocalTime_Formater;
    private DateTimeFormatter OffsetDateTime_Formater;
    private DateTimeFormatter OffsetTime_Formater;
    private DateTimeFormatter ZonedDateTime_Formater;
    private DateTimeFormatter Year_Formater;
    private DateTimeFormatter Month_Formater;
    private DateTimeFormatter MonthDay_Formater;
    private DateTimeFormatter YearMonth_Formater;
    private DateTimeFormatter DayOfWeek_Formater;
    private DateTimeFormatter Instant_Formater;

    private boolean useTimestamp;

    public TemporalAccessorNodeMapperOptions() {
        this.LocalDateTime_Formater = ISO_LOCAL_DATE_TIME;
        this.LocalDate_Formater = ISO_LOCAL_DATE;
        this.LocalTime_Formater = ISO_LOCAL_TIME;
        this.OffsetDateTime_Formater = ISO_OFFSET_DATE_TIME;
        this.OffsetTime_Formater = ISO_OFFSET_TIME;
        this.ZonedDateTime_Formater = ISO_ZONED_DATE_TIME;
        this.Year_Formater = DEFAULT_YEAR_FORMATER;
        this.Month_Formater = DEFAULT_MONTH_FORMATER;
        this.MonthDay_Formater = DEFAULT_MONTH_DAY_FORMATER;
        this.YearMonth_Formater = DEFAULT_YEAR_MONTH_FORMATER;
        this.DayOfWeek_Formater = DEFAULT_DAY_OF_WEEK_FORMATER;
        this.Instant_Formater = ISO_INSTANT;
        this.useTimestamp = false;
    }

    public TemporalAccessorNodeMapperOptions setFormatter(Class<? extends TemporalAccessor> type, DateTimeFormatter formatter) {
        if (type == LocalDateTime.class) {
            this.LocalDateTime_Formater = formatter;
        } else if (type == LocalDate.class) {
            this.LocalDate_Formater = formatter;
        } else if (type == LocalTime.class) {
            this.LocalTime_Formater = formatter;
        } else if (type == OffsetDateTime.class) {
            this.OffsetDateTime_Formater = formatter;
        } else if (type == OffsetTime.class) {
            this.OffsetTime_Formater = formatter;
        } else if (type == ZonedDateTime.class) {
            this.ZonedDateTime_Formater = formatter;
        } else if (type == Year.class) {
            this.Year_Formater = formatter;
        } else if (type == Month.class) {
            this.Month_Formater = formatter;
        } else if (type == MonthDay.class) {
            this.MonthDay_Formater = formatter;
        } else if (type == YearMonth.class) {
            this.YearMonth_Formater = formatter;
        } else if (type == DayOfWeek.class) {
            this.DayOfWeek_Formater = formatter;
        } else if (type == Instant.class) {
            this.Instant_Formater = formatter;
        }
        return this;
    }

    public DateTimeFormatter getFormatter(Class<? extends TemporalAccessor> type) {
        if (type == LocalDateTime.class) {
            return this.LocalDateTime_Formater;
        } else if (type == LocalDate.class) {
            return this.LocalDate_Formater;
        } else if (type == LocalTime.class) {
            return this.LocalTime_Formater;
        } else if (type == OffsetDateTime.class) {
            return this.OffsetDateTime_Formater;
        } else if (type == OffsetTime.class) {
            return this.OffsetTime_Formater;
        } else if (type == ZonedDateTime.class) {
            return this.ZonedDateTime_Formater;
        } else if (type == Year.class) {
            return this.Year_Formater;
        } else if (type == Month.class) {
            return this.Month_Formater;
        } else if (type == MonthDay.class) {
            return this.MonthDay_Formater;
        } else if (type == YearMonth.class) {
            return this.YearMonth_Formater;
        } else if (type == DayOfWeek.class) {
            return this.DayOfWeek_Formater;
        } else if (type == Instant.class) {
            return this.Instant_Formater;
        } else {
            return null;
        }
    }

    public TemporalAccessorNodeMapperOptions useTimestamp(boolean useTimestamp) {
        this.useTimestamp = useTimestamp;
        return this;
    }

    public boolean useTimestamp() {
        return useTimestamp;
    }

}
