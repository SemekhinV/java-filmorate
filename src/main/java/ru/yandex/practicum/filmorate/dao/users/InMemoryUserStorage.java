package ru.yandex.practicum.filmorate.dao.users;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorage {

    private final Map<Integer, User> userStorage;
    private Integer id;

    public InMemoryUserStorage() {

        this.userStorage = new HashMap<>();
        this.id = 0;
    }


    public User addEntity(User user) {

        user.setId(++id);
        userStorage.put(user.getId(), user);

        return user;
    }


    public Optional<User> getEntity(int id) {
        return Optional.ofNullable(userStorage.get(id));
    }


    public User removeEntity(int userId) {
        return userStorage.remove(userId);
    }


    public User updateEntity(User user) {
        userStorage.put(user.getId(), user);
        return user;
    }


    public Map<Integer, User> getAll() {
        return userStorage;
    }

}
