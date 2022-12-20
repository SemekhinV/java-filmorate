package ru.yandex.practicum.filmorate.dao.users;

import ru.yandex.practicum.filmorate.entity.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> addEntity(@Valid User entity);

    Optional<User> getUser(int id);

    List<User> getAll();

    Optional<User> updateUser(@Valid User entity);

    void addFriend(int userId, int friendId);

    List<Integer> getFriends(int userId);

    void removeFriend(int userId, int friendId);
}
