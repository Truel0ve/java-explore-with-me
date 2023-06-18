package ru.practicum.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.user.UserDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RateSorter {

    public List<UserDto> getSortedUsers(List<UserDto> userDtoList, Boolean asc) {
        if (asc == null) {
            return userDtoList;
        } else {
            if (asc.equals(Boolean.TRUE)) {
                return userDtoList
                        .stream()
                        .sorted(Comparator.comparing(UserDto::getRating, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else {
                return userDtoList
                        .stream()
                        .sorted(Comparator.comparing(UserDto::getRating))
                        .collect(Collectors.toList());
            }
        }
    }

    public List<EventShortDto> getSortedEventsShot(List<EventShortDto> eventShortDtoList, Boolean asc) {
        if (asc == null) {
            return eventShortDtoList;
        } else {
            if (asc.equals(Boolean.TRUE)) {
                return eventShortDtoList
                        .stream()
                        .sorted(Comparator.comparing(EventShortDto::getRating, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else {
                return eventShortDtoList
                        .stream()
                        .sorted(Comparator.comparing(EventShortDto::getRating))
                        .collect(Collectors.toList());
            }
        }
    }

    public List<EventFullDto> getSortedEventsFull(List<EventFullDto> eventFullDtoList, Boolean asc) {
        if (asc == null) {
            return eventFullDtoList;
        } else {
            if (asc.equals(Boolean.TRUE)) {
                return eventFullDtoList
                        .stream()
                        .sorted(Comparator.comparing(EventFullDto::getRating, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else {
                return eventFullDtoList
                        .stream()
                        .sorted(Comparator.comparing(EventFullDto::getRating))
                        .collect(Collectors.toList());
            }
        }
    }
}
