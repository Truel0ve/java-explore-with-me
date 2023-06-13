package ru.practicum.services.admin_service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user_dto.UserDto;
import ru.practicum.models.user.User;
import ru.practicum.repositories.user.UserRepository;
import ru.practicum.services.admin_service.api.AdminUserService;
import ru.practicum.utility.PageableBuilder;
import ru.practicum.utility.mapper.UserMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers(Set<Long> users, Integer from, Integer size) {
        Pageable pageable = PageableBuilder.getPageable(from, size);
        return userRepository.getUsers(users, pageable)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto postNewUser(UserDto user) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.getUserById(userId);
        userRepository.deleteById(user.getId());
    }
}
