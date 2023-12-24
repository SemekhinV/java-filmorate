package ru.yandex.practicum.user.service;

import ru.yandex.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto save(UserDto entity);

    UserDto getById(Long id);

    void delete(Long id);

    UserDto update(Long id, UserDto entity);

    UserDto addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<UserDto> getFriends(Long userId);
}
