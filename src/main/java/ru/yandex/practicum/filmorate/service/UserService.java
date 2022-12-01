package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.EntityExistException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements ServicePattern<User> {

    private final InMemoryUserStorage userStorage;

    private void isIdValid(int userId, int otherId) {
        if (!userStorage.getAll().containsKey(otherId)) {
            throw new EntityExistException("Ошибка добавления в друзья, пользователя с id = "
                    + otherId + " не существует");
        }

        if (!userStorage.getAll().containsKey(userId)) {
            throw new EntityExistException("Ошибка добавления в друзья, пользователя с id = "
                    + userId + " не существует");
        }
    }

    @Override
    public User addData(User user) {

        if ("".equals(user.getName())) {                                //Проверка на пустое имя
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {              //Проверка даты дня рождения
            throw new InvalidValueException("Invalid birthday data.");
        }

        if (userStorage.getAll().containsValue(user)) {          //Проверка на существования записи
            throw new EntityExistException("Post error, user " + user.getLogin() + " already exist.");
        }

        return userStorage.addEntity(user);
    }

    @Override
    public User updateData(User user) {

        if ("".equals(user.getName())) {                                //Проверка на пустое имя
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {              //Проверка даты дня рождения
            throw new InvalidValueException("Invalid birthday data.");
        }

        if (!userStorage.getAll().containsKey(user.getId())) {          //Проверка на существования записи
            throw new EntityExistException("Put error, user " + user.getLogin() + " does`t exist.");
        }

        return userStorage.updateEntity(user);
    }


    @Override
    public User getData(int id) {

        if (!userStorage.getAll().containsKey(id)) {
            throw new EntityExistException("Ошибка получения данных пользователя, запись с id = " + id + " не найдена");
        }

        return userStorage.getEntity(id);
    }

    @Override
    public User removeData(User user) {

        if (!userStorage.getAll().containsKey(user.getId())) {          //Проверка на существования записи
            throw new EntityExistException("Delete error, user " + user.getLogin() + " does`t exist.");
        }

        return userStorage.removeEntity(user);
    }

    @Override
    public Map<Integer, User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(int userId, int friendId) {

        isIdValid(userId, friendId);

        userStorage.getAll().get(userId).getFriends().add(friendId);
        userStorage.getAll().get(friendId).getFriends().add(userId);
    }

    public void removeFriend(int userId, int friendId) {

        isIdValid(userId, friendId);

        userStorage.getAll().get(userId).getFriends().remove(friendId);
        userStorage.getAll().get(friendId).getFriends().remove(userId);
    }

    public List<User> getFriends(int id) {                          //Создаем список пользователей на основе id,
        return userStorage.getEntity(id)                            //Лежащих в списке друщей, после чего с помощью
                .getFriends()                                       //Метода getData преобразуем id в пользователей
                .stream()
                .map(this::getData)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(int userId, int otherId) {

        isIdValid(userId, otherId);

        return userStorage                                          //Сначала фильтруем общих друзей, путем сравнивания
                .getEntity(userId)                                  //Списков друзей через предикат и метод .filter
                .getFriends()                                       //После чего преобразуем оставшиеся id в User
                .stream()                                           //И возвращаем новый список
                .filter(id -> userStorage.getEntity(otherId).getFriends().contains(id))
                .map(this::getData)
                .collect(Collectors.toList());
    }


}
