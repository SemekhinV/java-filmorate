package ru.yandex.practicum.filmorate.service.films;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import ru.yandex.practicum.filmorate.dao.filmgenre.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.filmlikes.FilmLikesDao;
import ru.yandex.practicum.filmorate.dao.users.UserDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.EntityExistException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.dao.films.FilmDao;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class FilmServiceImpl implements FilmService {

    private static final LocalDate DATE_BOUNDARY_VALUE = LocalDate.of(1895, 12, 28);

    private final FilmDao filmDao;
    private final MpaDao mpaDao;
    private final UserDao userDao;

    private final FilmLikesDao filmLikesDao;

    private final FilmGenreDao filmGenreDao;

    private int id;

    private void isValid(Film film, String flag) {

        if (film == null) {
            throw new EntityExistException("Ошибка обработки фильма, передано пустое значение.");
        }

        if (film.getReleaseDate().isBefore(DATE_BOUNDARY_VALUE)) {
            throw new InvalidValueException("Invalid release date");
        }

        switch (flag) {

            case "add" : {
                if (getAll().contains(film)) {

                    throw new EntityExistException("Post error, film " + film.getName() + " already exist.");
                }
                break;
            }
            case "with ID" : {
                if (filmDao.getFilm(film.getId()).isEmpty()) {
                    throw new EntityExistException("Ошибка обработки фильма фильма, запись с id = "
                            + film.getId() + " не существует.");
                }
                break;
            }
        }
    }

    private void isLikeOpValid(int userId, int filmId, String flag) {

        if (userId < 0 ) {
            throw new EntityExistException("Ошибка добавление лайка, некорректный ID.");
        }

        if (filmDao.getFilm(filmId).isEmpty()) {
            throw new EntityExistException("Ошибка добавления лайка фильму, фильм с id = " + filmId + " не найден.");
        }

        if (userDao.getUser(userId).isEmpty()) {
            throw new EntityExistException("Ошибка добавления лайка, пользователь с id = " + userId + " не найден.");
        }

        switch (flag) {
            case "remove" : {
                if (!getFilm(filmId).getLikes().contains(userId)) {
                    throw new EntityExistException("Ошибка уделания лайка," +
                            " пользователь с id = " + userId + " не оценивал фильм.");
                }
                break;
            }
            case "add" : {
                if (getFilm(filmId).getLikes().contains(userId)) {
                    throw new EntityExistException("Ошибка добавления лайка, пользователь может оценить фильм только один раз");
                }
                break;
            }
        }
    }

    @Override
    public Film addFilm(Film film) {

        isValid(film, "add");

        Mpa mpa = mpaDao
                .getById(film.getMpa().getId())
                .orElseThrow(() -> {
                    throw new EntityExistException("Ошибка добавления фильма, значение МРА не найдено.");
                });

        film.setMpa(mpa);

        film.setId(++id);

        filmDao.addFilm(film);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {

            List<Integer> genreIds = film.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .collect(Collectors.toList());

            filmGenreDao.addGenres(film.getId(), genreIds);
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {

        isValid(film, "with ID");


        Mpa mpa = mpaDao.getById(
                film.getMpa().getId()
                ).orElseThrow(() -> {
                    throw new EntityExistException("Ошибка поиска MPA при обновлении фильма" +
                            ", запись не найдена.");
                });

        film.setMpa(mpa);

        filmDao.updateFilm(film);

        film = filmGenreDao.updateFilmGenres(film);

        return film;
    }

    @Override
    public Film getFilm(int id) {

        Film searchableFilm = filmDao.getFilm(id).orElseThrow(
                () -> {throw new EntityExistException("Ошибка поиска фильма, запись с id =" + id + " не найдена.");}
        );

        searchableFilm.getGenres().addAll(filmGenreDao.getById(id));
        searchableFilm.getLikes().addAll(filmLikesDao.getUserLikes(id));

        return searchableFilm;
    }


    @Override
    public List<Film> getAll() {

        List<Film> allFilms = filmDao.getAll();

        allFilms.forEach(film -> {
            film.getGenres().addAll(filmGenreDao.getById(film.getId()));
            film.getLikes().addAll(filmLikesDao.getUserLikes(film.getId()));
        });

        return allFilms;
    }

    @Override
    public void addLike(int filmId, int userId) {

        isLikeOpValid(userId, filmId, "add");

        filmLikesDao.addLike(filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {

        isLikeOpValid(userId, filmId, "remove");

        filmLikesDao.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {

        List<Film> popularFilms = filmDao.getPopularFilms(count);

        popularFilms.forEach(
                film -> {
                    film.getGenres().addAll(filmGenreDao.getById(film.getId()));
                    film.getLikes().addAll(filmLikesDao.getUserLikes(film.getId()));
                }
        );

        return popularFilms;
    }
}
