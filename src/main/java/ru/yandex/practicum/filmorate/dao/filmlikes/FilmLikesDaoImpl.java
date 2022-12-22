package ru.yandex.practicum.filmorate.dao.filmlikes;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmLikesDaoImpl implements FilmLikesDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int filmId, int userId) {

        jdbcTemplate.update(
                "INSERT INTO film_likes (user_id, film_id) " +
                        "VALUES(?,?)", userId, filmId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        jdbcTemplate.update(
                "DELETE FROM film_likes " +
                        "WHERE user_id = ? AND film_id = ?", userId, filmId);
    }

    @Override
    public List<Integer> getUserLikes(int filmId) {

        return jdbcTemplate.query(
                "SELECT user_id " +
                        "FROM film_likes " +
                        "WHERE film_id = ?",
                (resultSet, rowNum) -> resultSet.getInt("user_id"), filmId);
    }
}
