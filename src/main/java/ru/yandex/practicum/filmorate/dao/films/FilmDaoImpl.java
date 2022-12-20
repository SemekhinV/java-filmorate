package ru.yandex.practicum.filmorate.dao.films;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.filmgenre.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.filmlikes.FilmLikesDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;

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

    private final FilmGenreDao filmGenreDao;

    private final FilmLikesDao filmLikesDao;

    @Override
    public Film addFilm(Film entity) {
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

            filmGenreDao.addGenres(entity.getId(), genreIds);
        }
        return entity;
    }

    @Override
    public Film updateFilm(Film entity) {

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

        List<Integer> filmGenres = filmGenreDao.getById(entity.getId())
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        List<Integer> filmsAdded = entity.getGenres()
                .stream()
                .map(Genre::getId)
                .filter(genre -> !filmGenres.contains(genre))
                .distinct()
                .collect(Collectors.toList());

        filmGenreDao.addGenres(entity.getId(), filmsAdded);

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
        filmGenreDao.removeGenres(entity.getId(), filmsRemoved);

        entity.getGenres().clear();

        for (Genre g : filmGenreDao.getById(entity.getId())) {
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

            film.getGenres().addAll(filmGenreDao.getById(filmId));
            film.getLikes().addAll(filmLikesDao.getUserLikes(filmId));
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void addLike(int filmId, int userId) {
        filmLikesDao.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        filmLikesDao.deleteLike(filmId,userId);
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
            film.getGenres().addAll(filmGenreDao.getById(film.getId()));
            film.getLikes().addAll(filmLikesDao.getUserLikes(film.getId()));
        });

        return films;
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

        film.getGenres().addAll(filmGenreDao.getById(resultSet.getInt("id")));
        film.getLikes().addAll(filmLikesDao.getUserLikes(resultSet.getInt("id")));

        return film;
    }
}
