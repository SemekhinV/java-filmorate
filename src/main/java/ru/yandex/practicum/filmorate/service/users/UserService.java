package ru.yandex.practicum.filmorate.service.users;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;

public interface UserService {
    //В качестве названия методов невозможно использовать просто add, delete
    //И т.д., потому что возникает конфликт с сервисными методами
    User addUser(User entity);             //Аналогично реализации хранилища,

    //Используем общий интерфейс для описания
    //Функционала обобщенного сервиса
    User getUser(int id);

    User updateUser(User user);

    List<User> getAll();

    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    List<User> getUserFriends(int id);

    List<User> getMutualFriends(int userId, int otherId);
}
