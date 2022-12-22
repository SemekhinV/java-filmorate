package ru.yandex.practicum.filmorate.dao.userfriends;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;

public interface UserFriendsDao {
    void addFriend(int userId, int friendId);

    User updateUserFriends(User user);

    List<Integer> getFriends(int userId);

    void removeFriend(int userId, int friendId);
}
