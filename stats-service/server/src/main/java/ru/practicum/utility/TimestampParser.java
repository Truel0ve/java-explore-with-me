package ru.practicum.utility;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TimestampParser {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Timestamp toTimestamp(String s) {
        return Timestamp.valueOf(LocalDateTime.parse(s, dateTimeFormatter));
    }

    public String toString(Timestamp timestamp) {
        return dateTimeFormatter.format(timestamp.toLocalDateTime());
    }
}
