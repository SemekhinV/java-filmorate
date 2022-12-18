package ru.yandex.practicum.filmorate.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.users.UserDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.EntityExistException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private int id;

    private void isUserValid(User user, String flag) {

        if (user == null) {
            throw new EntityExistException("Ошибка обработки пользователя, передано пустое значение.");
        }

        if ("".equals(user.getName())) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidValueException("Некорректная дата рождения пользователя.");
        }

        switch (flag) {
            case "add" : {
                if (userDao.getEntity(user.getId()).isPresent()) {
                    throw new EntityExistException("Ошибка получения данных пользователя, запись с id = "
                            + user.getId() + " уже существует");
                }
                break;
            }
            case "update" : {
                if (userDao.getEntity(user.getId()).isEmpty()) {
                    throw new EntityExistException("Ошибка получения данных пользователя, запись с id = " + user.getId()
                            + " не найдена");
                }
                break;
            }
        }
    }

    private void isIdValid(int userId, int otherId) {

        if (userDao.getEntity(userId).isEmpty()) {
            throw new EntityExistException("Ошибка добавления в друзья, пользователя с id = "
                    + otherId + " не существует");
        }

        if (otherId != -1 && userDao.getEntity(otherId).isEmpty()) {
            throw new EntityExistException("Ошибка добавления в друзья, пользователя с id = "
                    + otherId + " не существует");
        }
    }

    @Override
    public User addData(User user) {

        isUserValid(user, "add");

        user.setId(++id);

        return userDao.addEntity(user).get();       //Можем использовать get() без isPresent т.к. проверки уже пройдены
    }

    @Override
    public User updateData(User user) {

        isUserValid(user, "update");

        return userDao.updateEntity(user).get();
    }


    @Override
    public User getData(int id) {

        isIdValid(id, -1);

        return userDao.getEntity(id).get();
    }

    @Override
    public List<User> getAll() {

        return userDao.getAll();
    }

    @Override
    public void addFriend(int userId, int friendId) {

        isIdValid(userId, friendId);

        userDao.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {

        isIdValid(userId, friendId);

        userDao.removeFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {

        isIdValid(userId, -1);

        return userDao
                .getFriends(userId)
                .stream()
                .map(userDao::getEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getMutualFriends(int userId, int friendId) {

        isIdValid(userId, friendId);

        User user = getData(userId);
        User friend = getData(friendId);

        return user.getFriends()
                .stream()
                .filter(id -> friend.getFriends().contains(id))
                .map(userDao::getEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


}
