package ru.yandex.practicum.filmorate.dao.films;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FilmDaoImpl implements FilmDao{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addEntity(Film entity) {
        jdbcTemplate.update("INSERT INTO film (id, name, description, duration, release_date, rating_id) " +
                        "VALUES(?,?,?,?,?,?)",
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDuration(),
                entity.getReleaseDate(),
                entity.getMpa().getId());

        if (entity.getGenres() != null) {
            List<Integer> genreIds = entity.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .collect(Collectors.toList());

            addGenres(entity.getId(), genreIds);
        }
        return entity;
    }

    @Override
    public Film updateEntity(Film entity) {

        jdbcTemplate.update(
                "UPDATE film " +
                        "SET name = ?, " +
                        "description = ?, " +
                        "duration = ?, " +
                        "release_date = ?, " +
                        "rating_id = ? " +
                        "WHERE id = ?",
                entity.getName(),
                entity.getDescription(),
                entity.getDuration(),
                entity.getReleaseDate(),
                entity.getMpa().getId(),
                entity.getId());

        List<Integer> filmGenres = getFilmGenres(entity.getId())
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        List<Integer> filmsAdded = entity.getGenres()
                .stream()
                .map(Genre::getId)
                .filter(genre -> !filmGenres.contains(genre))
                .distinct()
                .collect(Collectors.toList());

        addGenres(entity.getId(), filmsAdded);

        List<Integer> newGenreList = entity
                .getGenres()
                .stream()
                .distinct()
                .map(Genre::getId)
                .collect(Collectors.toList());

        List<Integer> filmsRemoved = filmGenres
                .stream()
                .filter(genre -> !newGenreList.contains(genre))
                .collect(Collectors.toList());
        removeGenres(entity.getId(), filmsRemoved);

        entity.getGenres().clear();

        for (Genre g : getFilmGenres(entity.getId())) {
            entity.getGenres().add(g);
        }

        return entity;
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
    public Optional<Film> getEntity(int filmId) {
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

            film.getGenres().addAll(getFilmGenres(filmId));
            film.getLikes().addAll(getUserLikes(filmId));
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

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
    public List<Film> getPopularFilms(int count) {

        List<Film> films = jdbcTemplate.query(
                "SELECT f.*, " +
                        "m.name AS mpa_name " +
                        "FROM film AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.id " +
                        "LEFT OUTER JOIN film_likes AS fl ON f.id = fl.film_id " +
                        "GROUP BY f.id, fl.user_id " +
                        "ORDER BY COUNT(fl.user_id) DESC " +
                        "LIMIT ?", (resultSet, rowNum) -> mapFilm(resultSet), count);

        films.forEach(film -> {
            film.getGenres().addAll(getFilmGenres(film.getId()));
            film.getLikes().addAll(getUserLikes(film.getId()));
        });

        return films;
    }

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

    private Film mapFilm(ResultSet resultSet) throws SQLException {

        Film film = Film.builder()
                .id(resultSet.getInt("id"))
                .mpa(new Mpa(resultSet.getInt("rating_id"), resultSet.getString("mpa_name")))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .name(resultSet.getString("name"))
                .build();

        film.getGenres().addAll(getFilmGenres(resultSet.getInt("id")));
        film.getLikes().addAll(getUserLikes(resultSet.getInt("id")));

        return film;
    }

    public List<Genre> getFilmGenres(int filmId) {

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

    private List<Integer> getUserLikes(int filmId) {

        return jdbcTemplate.query(
                "SELECT user_id " +
                        "FROM film_likes " +
                        "WHERE film_id = ?",
                (resultSet, rowNum) -> resultSet.getInt("user_id"), filmId);
    }
}
