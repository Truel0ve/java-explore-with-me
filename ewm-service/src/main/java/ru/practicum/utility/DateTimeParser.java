package ru.practicum.utility;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeParser {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime toLocalDateTime(String s) {
        if (s != null) {
            return LocalDateTime.parse(s, dateTimeFormatter);
        } else {
            return null;
        }
    }

    public String toString(Timestamp timestamp) {
        return dateTimeFormatter.format(timestamp.toLocalDateTime());
    }

    public String toString(LocalDateTime localDateTime) {
        return dateTimeFormatter.format(localDateTime);
    }
}
