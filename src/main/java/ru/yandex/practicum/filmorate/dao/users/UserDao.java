package ru.yandex.practicum.filmorate.dao.users;

import ru.yandex.practicum.filmorate.entity.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> addUser(@Valid User user);

    Optional<User> getUser(int id);

    List<User> getAll();

    Optional<User> updateUser(@Valid User user);

}
