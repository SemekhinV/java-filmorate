package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {


    private final UserService service;

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {

        log.info("Post new user - {}", user.getLogin());                //Логирование
        log.debug("Post user data: {}", user);                          //По логике работы логера конкатенация будет
        //Вызвана только при указании уровня логирования
        //Debug, что уменьшит нагрузку на систему
        return service.addData(user);
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {

        log.info("Put new user - {}", user.getLogin());                  //Логирование
        log.debug("Put user data: {}", user);

        return service.updateData(user);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return service.getAll().values();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {

        log.debug("Запрос на получение данных пользователя с id = {}", userId);
        return service.getData(userId);
    }

    @DeleteMapping
    public User deleteFilmById(@Valid @RequestBody User user) {

        log.debug("Удаление пользователя userId = {}, userData = {}", user.getId(), user);

        return service.removeData(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {

        log.info("Новая заявка на добавления в друзья");
        log.debug("Пользователю userId = {} добавлен новый друг с friendId = {}", userId, friendId);

        service.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId, @PathVariable int friendId) {

        log.info("Пользователь удалил своего друга.");
        log.debug("У пользователя userId = {} был удален друг с friendId = {}", userId, friendId);

        service.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") int userId) {

        log.info("Запрос на отображение списка друзей");
        log.debug("Запрос списка друзей у пользователя userId = {}", userId);

        return service.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    private List<User> getMutualFriends(@PathVariable("id") int userId, @PathVariable int otherId) {

        log.info("Запрос на отображение списка общих друзей");
        log.debug("Пользователь userId = {} отправил запрос на отображение списка общих друзей с otherId = {}",
                userId, otherId);

        return service.getMutualFriends(userId, otherId);
    }

}
