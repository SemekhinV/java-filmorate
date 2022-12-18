package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.dao.users.UserDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.users.UserService;
import ru.yandex.practicum.filmorate.service.users.UserServiceImpl;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {

        log.info("Post new user - {}", user);                //Логирование

        return service.addData(user);
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {

        log.info("Put new user - {}", user);                  //Логирование

        return service.updateData(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return service.getAll();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {

        log.info("Запрос на получение данных пользователя с id = {}", userId);
        return service.getData(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {

        log.info("Пользователю userId = {} добавлен новый друг с friendId = {}", userId, friendId);

        service.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId, @PathVariable int friendId) {

        log.info("У пользователя userId = {} был удален друг с friendId = {}", userId, friendId);

        service.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") int userId) {

        log.info("Запрос списка друзей у пользователя userId = {}", userId);

        return service.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") int userId, @PathVariable int otherId) {

        log.info("Пользователь userId = {} отправил запрос на отображение списка общих друзей с otherId = {}",
                userId, otherId);

        return service.getMutualFriends(userId, otherId);
    }

}
