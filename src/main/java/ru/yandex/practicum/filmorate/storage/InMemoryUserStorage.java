package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements Storage<User> {

    private final Map<Integer, User> userStorage;
    private Integer id;

    public InMemoryUserStorage() {

        this.userStorage = new HashMap<>();
        this.id = 0;
    }

    @Override
    public User addEntity(User user) {

        user.setId(++id);
        userStorage.put(user.getId(), user);

        return user;
    }

    @Override
    public User getEntity(int id) {
        return userStorage.get(id);
    }

    @Override
    public User removeEntity(User user) {
        return userStorage.remove(user.getId());
    }

    @Override
    public User updateEntity(User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public Map<Integer, User> getAll() {
        return userStorage;
    }

}
