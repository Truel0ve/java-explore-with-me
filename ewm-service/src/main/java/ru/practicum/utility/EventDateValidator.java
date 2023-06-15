package ru.practicum.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.exceptions.WrongStateArgumentException;

import java.time.Duration;
import java.time.LocalDateTime;

@UtilityClass
public class EventDateValidator {

    public LocalDateTime validateEventDate(String stringDateTime, Duration duration) {
        LocalDateTime localDateTime = DateTimeParser.toLocalDateTime(stringDateTime);
        if (localDateTime.isBefore(LocalDateTime.now().plus(duration))) {
            throw new WrongStateArgumentException(
                    "The event date cannot be earlier than " + DateTimeParser.toString(LocalDateTime.now().plus(duration)),
                    new IllegalArgumentException());
        }
        return localDateTime;
    }
}
