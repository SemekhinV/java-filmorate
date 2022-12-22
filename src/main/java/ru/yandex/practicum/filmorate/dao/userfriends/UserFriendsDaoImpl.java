package ru.yandex.practicum.filmorate.dao.userfriends;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserFriendsDaoImpl implements UserFriendsDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int userId, int friendId) {

        jdbcTemplate.update(
                "INSERT INTO user_friends (user_id, friends_id) " +
                        "VALUES (?,?)",
                userId, friendId
        );
    }

    @Override
    public User updateUserFriends(User user) {

        List<Integer> userFriends = getFriends(user.getId());

        List<Integer> friendsAdded = user.getFriends()
                .stream()
                .filter(friend -> !userFriends.contains(friend))
                .collect(Collectors.toList());

        friendsAdded.forEach(friend -> addFriend(user.getId(), friend));

        userFriends                     //Находим значения, которых нет в новом списке друзей и удаляем их
                .stream()
                .filter(friend -> !user.getFriends().contains(friend))
                .collect(Collectors.toList())
                .forEach(friend -> removeFriend(user.getId(), friend));

        user.getFriends().clear();

        user.getFriends().addAll(getFriends(user.getId()));

        return user;
    }

    @Override
    public List<Integer> getFriends(int userId) {

        return jdbcTemplate.query(
                "SELECT friends_id " +
                        "FROM user_friends " +
                        "WHERE user_id = ?",
                (resultSet, rowNum) -> resultSet.getInt("friends_id"), userId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {

        jdbcTemplate.update(
                "DELETE FROM user_friends " +
                        "WHERE user_id = ? AND friends_id = ?",
                userId,
                friendId
        );
    }
}
