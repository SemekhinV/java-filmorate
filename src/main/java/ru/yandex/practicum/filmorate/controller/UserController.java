package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.InvalidUserBirthdayDataException;
import ru.yandex.practicum.filmorate.entity.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final HashMap<Integer, User> userList;
    private int id;

    public UserController() {
       this.userList = new HashMap<>();
       this.id = 0;
    }

    @PostMapping
    public User postUser(@Valid  @RequestBody User user) {

        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }

        if (userList.containsKey(user.getId())) {
            log.error("User {} already exist.", user.getLogin());
            log.debug("User AE data: {}", user);
            throw new EntityAlreadyExistException("Post error, user " + user.getLogin() + " already exist.");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("EXCEPTION: Invalid birthday data.");
            throw new InvalidUserBirthdayDataException("Invalid birthday data.");
        }

        log.info("Post new user - {}", user.getLogin());                //Логирование
        log.debug("Post user data: {}", user);                          //По логике работы логера конкатенация будет
                                                                        //Вызвана только при указании уровня логирования
                                                                        //Debug, что уменьшит нагрузку на систему
        user.setId(++id);
        userList.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {

        if ("".equals(user.getName())) {                                //Проверка на пустое имя
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {              //Проверка даты дня рождения
            log.error("EXCEPTION: Invalid birthday data.");
            throw new InvalidUserBirthdayDataException("Invalid birthday data.");
        }

        if (!userList.containsKey(user.getId())) {
            log.error("Put error, user {} does`t exist", user.getLogin());
            log.debug("User put error data: {}", user);
            throw new EntityAlreadyExistException("Put error, user " + user.getLogin() + " does`t exist.");
        }

        log.info("Put new user - {}", user.getLogin());                  //Логирование
        log.debug("Put user data: {}", user);

        userList.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public ArrayList<User> getUsers() {
        return new ArrayList<>(userList.values());
    }
}
