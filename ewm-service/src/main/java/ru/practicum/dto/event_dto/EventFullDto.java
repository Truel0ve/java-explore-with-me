package ru.practicum.dto.event_dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.category_dto.CategoryDto;
import ru.practicum.dto.user_dto.UserShortDto;
import ru.practicum.models.location.Location;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;
    String title;
    String annotation;
    CategoryDto category;
    String description;
    String eventDate;
    String createdOn;
    String publishedOn;
    Location location;
    Boolean paid;
    Boolean requestModeration;
    Long participantLimit;
    UserShortDto initiator;
    String state;
    Long confirmedRequests;
    Long views;
}
