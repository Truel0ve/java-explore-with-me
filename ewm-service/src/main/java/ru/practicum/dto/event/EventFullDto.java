package ru.practicum.dto.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.models.location.Location;

@Getter
@Setter
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
    Long rating;
}
