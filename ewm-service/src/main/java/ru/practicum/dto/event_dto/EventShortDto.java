package ru.practicum.dto.event_dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.category_dto.CategoryDto;
import ru.practicum.dto.user_dto.UserShortDto;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    Long id;
    String title;
    String annotation;
    CategoryDto category;
    String eventDate;
    Boolean paid;
    Long confirmedRequests;
    UserShortDto initiator;
    Long views;
}
