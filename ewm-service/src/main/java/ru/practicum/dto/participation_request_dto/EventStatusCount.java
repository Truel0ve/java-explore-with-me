package ru.practicum.dto.participation_request_dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventStatusCount {
    Long eventId;
    Long statusCount;

    public EventStatusCount(Long eventId, Long statusCount) {
        this.eventId = eventId;
        this.statusCount = statusCount;
    }
}
