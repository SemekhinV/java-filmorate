package ru.yandex.practicum.filmorate.dao.users;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
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

    @Override
    public Optional<User> addUser(User user) {

        jdbcTemplate.update(
                "INSERT INTO users (id, email, login, name, birthday) VALUES (?,?,?,?,?)",
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()));

        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User user) {

        jdbcTemplate.update(
                "UPDATE users " +
                        "SET email= ?, " +
                        "login = ?, " +
                        "name = ?, " +
                        "birthday = ?" +
                        "WHERE id = ?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());

        return getUser(user.getId());
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


    private User mappingUser(ResultSet resultSet) throws SQLException {

        return User.builder()
                .id(resultSet.getInt("id"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
