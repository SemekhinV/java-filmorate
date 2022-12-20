package ru.yandex.practicum.filmorate.dao.filmgenre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmGenreDaoImpl implements FilmGenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addGenres(int filmId, List<Integer> genres) {

        jdbcTemplate.batchUpdate(

                "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)",

                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int index) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genres.get(index));
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    @Override
    public void removeGenres(int filmId, List<Integer> genres) {
        jdbcTemplate.batchUpdate(

                "DELETE FROM film_genre WHERE film_id = ? AND genre_id = ?",

                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int value) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genres.get(value));
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    @Override
    public List<Genre> getById(int filmId) {

        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM genre " +
                        "INNER JOIN film_genre ON film_genre.genre_id = genre.id " +
                        "AND film_genre.film_id = ?",
                (resultSet, rowNum) -> mappingGenre(resultSet), filmId);
    }

    private Genre mappingGenre(ResultSet resultSet) throws SQLException {

        return new Genre(resultSet.getInt("id"),
                resultSet.getString("name"));
    }
}
