package ru.yandex.practicum.filmorate.dao.userfriends;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFriendsDaoImpl implements UserLikesDao {

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
