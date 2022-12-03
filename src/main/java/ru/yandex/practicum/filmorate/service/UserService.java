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

    private void isUserValid(User user, String flag) {

        if ("".equals(user.getName())) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidValueException("Некорректная дата рождения пользователя.");
        }

        switch (flag) {
            case "add" -> {
                if (userStorage.getAll().containsValue(user)) {
                    throw new EntityExistException("Ошибка получения данных пользователя, запись с id = "
                            + user.getId() + " уже существует");
                }
            }
            case "update" -> {
                if (!userStorage.getAll().containsKey(user.getId())) {
                    throw new EntityExistException("Ошибка получения данных пользователя, запись с id = " + user.getId()
                            + " не найдена");
                }
            }
        }
    }

    private void isIdValid(int userId, int otherId, String flag) {

        switch (flag) {

            case "likes" -> {
                if (!userStorage.getAll().containsKey(otherId)) {
                    throw new EntityExistException("Ошибка добавления в друзья, пользователя с id = "
                            + otherId + " не существует");
                }

                if (!userStorage.getAll().containsKey(userId)) {
                    throw new EntityExistException("Ошибка добавления в друзья, пользователя с id = "
                            + userId + " не существует");
                }
            }

            case "isUserExist" -> {
                if (!userStorage.getAll().containsKey(userId)) {
                    throw new EntityExistException("Ошибка получения данных пользователя, запись с id = " + userId + " не найдена");
                }
            }
        }
    }


    @Override
    public User addData(User user) {

        isUserValid(user, "add");

        return userStorage.addEntity(user);
    }

    @Override
    public User updateData(User user) {

        isUserValid(user, "update");

        return userStorage.updateEntity(user);
    }


    @Override
    public User getData(int id) {

        isIdValid(id, 0, "isUserExist");

        return userStorage.getEntity(id);
    }

    @Override
    public User removeData(User user) {

        isIdValid(user.getId(), 0, "isUserExist");

        return userStorage.removeEntity(user);
    }

    @Override
    public Map<Integer, User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(int userId, int friendId) {

        isIdValid(userId, friendId, "likes");

        userStorage.getAll().get(userId).getFriends().add(friendId);
        userStorage.getAll().get(friendId).getFriends().add(userId);
    }

    public void removeFriend(int userId, int friendId) {

        isIdValid(userId, friendId, "likes");

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

        isIdValid(userId, otherId, "likes");

        return userStorage                                          //Сначала фильтруем общих друзей, путем сравнивания
                .getEntity(userId)                                  //Списков друзей через предикат и метод .filter
                .getFriends()                                       //После чего преобразуем оставшиеся id в User
                .stream()                                           //И возвращаем новый список
                .filter(id -> userStorage.getEntity(otherId).getFriends().contains(id))
                .map(this::getData)
                .collect(Collectors.toList());
    }


}
