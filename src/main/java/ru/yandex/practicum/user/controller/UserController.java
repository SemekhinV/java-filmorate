package ru.yandex.practicum.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@Valid @RequestBody UserDto user) {

        log.info("Post new user - {}", user);

        return userService.save(user);
    }

    @PutMapping("/{userId}")
    public UserDto update(@Valid @RequestBody UserDto user, @PathVariable Long userId) {

        log.info("Put new user - {}", user);

        return userService.update(userId, user);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Long userId) {

        log.info("Запрос на получение данных пользователя с id = {}", userId);

        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long userId) {

        log.info("Удаление пользователя userId = {}", userId);

        userService.delete(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public UserDto addFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {

        log.info("Пользователю userId = {} добавлен новый друг с friendId = {}", userId, friendId);

        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {

        log.info("У пользователя userId = {} был удален друг с friendId = {}", userId, friendId);

        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<UserDto> getUserFriends(@PathVariable("id") Long userId) {

        log.info("Запрос списка друзей у пользователя userId = {}", userId);

        return userService.getFriends(userId);
    }
}
