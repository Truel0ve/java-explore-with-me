package ru.practicum.exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.utility.DateTimeParser;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {
    String status;
    String reason;
    String message;
    String timestamp;
    List<String> errors;

    public ApiError(String status, String reason, String message, List<String> errors) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = DateTimeParser.toString(LocalDateTime.now());
        this.errors = errors;
    }
}
