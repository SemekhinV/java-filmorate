package ru.yandex.practicum.filmorate.dao.films;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class FilmDaoImpl implements FilmDao{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update("INSERT INTO film (id, name, description, duration, release_date, rating_id) " +
                        "VALUES(?,?,?,?,?,?)",
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId());

        return film;
    }

    @Override
    public Film updateFilm(Film film) {

        jdbcTemplate.update(
                "UPDATE film " +
                        "SET name = ?, " +
                        "description = ?, " +
                        "duration = ?, " +
                        "release_date = ?, " +
                        "rating_id = ? " +
                        "WHERE id = ?",
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());

        return film;
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(
                "SELECT f.*, " +
                        "m.name AS mpa_name, " +
                        "FROM FILM AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.id", (resultSet, rowNum) -> mapFilm(resultSet));
    }

    @Override
    public Optional<Film> getFilm(int filmId) {
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(
                "SELECT f.*, m.name AS mpa_name " +
                        "FROM film AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.id " +
                        "AND f.id = ?", filmId);

        if (filmRow.next()) {
            Film film = Film.builder()
                    .id(filmRow.getInt("id"))
                    .name(Objects.requireNonNull(filmRow.getString("name")))
                    .description(Objects.requireNonNull(filmRow.getString("description")))
                    .duration(filmRow.getInt("duration"))
                    .releaseDate(Objects.requireNonNull(filmRow.getDate("release_date")).toLocalDate())
                    .mpa(new Mpa(filmRow.getInt("rating_id"), filmRow.getString(7)))
                    .build();

            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {

       return jdbcTemplate.query(
                "SELECT f.*, " +
                        "m.name AS mpa_name " +
                        "FROM film AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.id " +
                        "LEFT OUTER JOIN film_likes AS fl ON f.id = fl.film_id " +
                        "GROUP BY f.id, fl.user_id " +
                        "ORDER BY COUNT(fl.user_id) DESC " +
                        "LIMIT ?", (resultSet, rowNum) -> mapFilm(resultSet), count);
    }

    private Film mapFilm(ResultSet resultSet) throws SQLException {

        return Film.builder()
                .id(resultSet.getInt("id"))
                .mpa(new Mpa(resultSet.getInt("rating_id"), resultSet.getString("mpa_name")))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .name(resultSet.getString("name"))
                .build();
    }
}
