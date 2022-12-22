package ru.yandex.practicum.filmorate.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.userfriends.UserFriendsDao;
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

    private final UserFriendsDao userFriendsDao;

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
                if (userDao.getUser(user.getId()).isPresent()) {
                    throw new EntityExistException("Ошибка получения данных пользователя, запись с id = "
                            + user.getId() + " уже существует");
                }
                break;
            }
            case "update" : {
                if (userDao.getUser(user.getId()).isEmpty()) {
                    throw new EntityExistException("Ошибка получения данных пользователя, запись с id = " + user.getId()
                            + " не найдена");
                }
                break;
            }
        }
    }

    private void isIdValid(int userId, int friendId) {

        if (userId < 1) {
            throw new EntityExistException("Ошибка при обработке друзей, некорректное значение id.");
        }

        if (friendId != -101 && (userId + friendId) <=2) {
            throw new EntityExistException("Ошибка при обработке друзей, некорректное значение id.");
        }

        if (userDao.getUser(userId).isEmpty()) {
            throw new EntityExistException("Ошибка добавления в друзья, пользователя с id = "
                    + friendId + " не существует");
        }

        if (friendId != -101 && userDao.getUser(friendId).isEmpty()) {
            throw new EntityExistException("Ошибка добавления в друзья, пользователя с id = "
                    + friendId + " не существует");
        }
    }

    @Override
    public User addUser(User user) {

        isUserValid(user, "add");

        user.setId(++id);

        if (user.getFriends() != null && !user.getFriends().isEmpty()) {
            user.getFriends().forEach(friendId -> userFriendsDao.addFriend(user.getId(), friendId));
        }

        return userDao.addUser(user).get();       //Можем использовать get() без isPresent т.к. проверки уже пройдены
    }

    @Override
    public User updateUser(User user) {

        isUserValid(user, "update");

        if (user.getFriends() != null) {
            user = userFriendsDao.updateUserFriends(user);
        }

        return userDao.updateUser(user).get();
    }


    @Override
    public User getUser(int id) {

        isIdValid(id, -101);

        User searchableUser = userDao.getUser(id).orElseThrow(
                () -> {throw new EntityExistException("Ошибка поиска юзера, запись с id = " + id + " не найдена.");}
        );

        searchableUser.getFriends().addAll(userFriendsDao.getFriends(id));

        return searchableUser;
    }

    @Override
    public List<User> getAll() {

        List<User> allUser = userDao.getAll();

        allUser.forEach(user -> user.getFriends().addAll(userFriendsDao.getFriends(user.getId())));

        return allUser;
    }

    @Override
    public void addFriend(int userId, int friendId) {

        isIdValid(userId, friendId);

        userFriendsDao.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {

        isIdValid(userId, friendId);

        userFriendsDao.removeFriend(userId, friendId);
    }

    @Override
    public List<User> getUserFriends(int userId) {

        isIdValid(userId, -101);

        return userFriendsDao
                .getFriends(userId)
                .stream()
                .map(userDao::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getMutualFriends(int userId, int friendId) {

        isIdValid(userId, friendId);

        User user = getUser(userId);
        User friend = getUser(friendId);

        return user.getFriends()
                .stream()
                .filter(id -> friend.getFriends().contains(id))
                .map(this::getUser)
                .collect(Collectors.toList());
    }


}
