package ru.practicum.services.admin_service.api;

import ru.practicum.dto.user_dto.UserDto;

import java.util.List;
import java.util.Set;

public interface AdminUserService {
    List<UserDto> getUsers(Set<Long> users, Integer from, Integer size);
    UserDto postNewUser(UserDto user);
    void deleteUser(Long userId);
}
