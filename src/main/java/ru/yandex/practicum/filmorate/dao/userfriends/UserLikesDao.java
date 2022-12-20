package ru.yandex.practicum.filmorate.dao.userfriends;

import java.util.List;

public interface UserLikesDao {
    void addFriend(int userId, int friendId);

    List<Integer> getFriends(int userId);

    void removeFriend(int userId, int friendId);
}
