package ru.practicum.services.admin.api;

import ru.practicum.dto.user.UserDto;

import java.util.List;
import java.util.Set;

public interface AdminUserService {

    List<UserDto> getUsers(Set<Long> users, Integer from, Integer size, Boolean rate);

    UserDto postNewUser(UserDto user);

    void deleteUser(Long userId);
}