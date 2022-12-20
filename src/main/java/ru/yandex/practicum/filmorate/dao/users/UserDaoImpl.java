package ru.yandex.practicum.filmorate.dao.users;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.userfriends.UserLikesDao;
import ru.yandex.practicum.filmorate.entity.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao{

    private final JdbcTemplate jdbcTemplate;

    private final UserLikesDao userLikesDao;

    @Override
    public Optional<User> addEntity(User entity) {

        jdbcTemplate.update(
                "INSERT INTO users (id, email, login, name, birthday) VALUES (?,?,?,?,?)",
                entity.getId(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getName(),
                Date.valueOf(entity.getBirthday()));

        return Optional.of(entity);
    }

    @Override
    public Optional<User> updateUser(User entity) {

        jdbcTemplate.update(
                "UPDATE users " +
                        "SET email= ?, " +
                        "login = ?, " +
                        "name = ?, " +
                        "birthday = ?" +
                        "WHERE id = ?",
                entity.getEmail(),
                entity.getLogin(),
                entity.getName(),
                Date.valueOf(entity.getBirthday()),
                entity.getId());

        return getUser(entity.getId());
    }

    @Override
    public Optional<User> getUser(int id) {

        SqlRowSet userRow = jdbcTemplate.queryForRowSet(
                "SELECT * FROM users WHERE id = ?", id);

        if (userRow.next()) {

            User user = User.builder()
                    .id(userRow.getInt("id"))
                    .login(Objects.requireNonNull(userRow.getString("login")))
                    .name(userRow.getString("name"))
                    .email(Objects.requireNonNull(userRow.getString("email")))
                    .birthday(Objects.requireNonNull(userRow.getDate("birthday")).toLocalDate())
                    .build();

            user.getFriends().addAll(getFriends(id));
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {

        return jdbcTemplate.query("SELECT * FROM users",
                (rs, rowNum) -> mappingUser(rs));
    }

    @Override
    public void addFriend(int userId, int friendId) {
        userLikesDao.addFriend(userId, friendId);
    }

    @Override
    public List<Integer> getFriends(int userId) {
        return userLikesDao.getFriends(userId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        userLikesDao.removeFriend(userId, friendId);
    }

    private User mappingUser(ResultSet resultSet) throws SQLException {

        User user = User.builder()
                .id(resultSet.getInt("id"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();

        user.getFriends().addAll(getFriends(resultSet.getInt("id")));
        return user;
    }
}
