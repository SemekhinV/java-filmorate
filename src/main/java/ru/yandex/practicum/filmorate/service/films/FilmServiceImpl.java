package ru.yandex.practicum.filmorate.service.films;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import ru.yandex.practicum.filmorate.dao.users.UserDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.EntityExistException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.dao.films.FilmDao;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;

import javax.validation.Valid;

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

    private int id;

    private void isValid(Film film, String flag) {

        if (film == null) {
            throw new InvalidValueException("Ошибка обработки фильма, передано пустое значение.");
        }

        if (film.getReleaseDate().isBefore(DATE_BOUNDARY_VALUE)) {
            throw new InvalidValueException("Invalid release date");
        }

        switch (flag) {

            case "add" : {
                if (filmDao.getAll().contains(film)) {

                    throw new EntityExistException("Post error, film " + film.getName() + " already exist.");
                }
                break;
            }
            case "with ID" : {
                if (filmDao.getEntity(film.getId()).isEmpty()) {
                    throw new EntityExistException("Ошибка обработки фильма фильма, запись с id = "
                            + film.getId() + " не существует.");
                }
                break;
            }
        }
    }

    private void isLikeOpValid(int userId, int filmId, String flag) {

        if (userId < 0 ) {
            throw new InvalidValueException("Ошибка добавление лайка, некорректный ID.");
        }

        if (filmDao.getEntity(filmId).isEmpty()) {
            throw new EntityExistException("Ошибка добавления лайка фильму, фильм с id = " + filmId + " не найден.");
        }

        if (userDao.getEntity(userId).isEmpty()) {
            throw new EntityExistException("Ошибка добавления лайка, пользователь с id = " + userId + " не найден.");
        }

        switch (flag) {
            case "remove" : {
                if (!filmDao.getEntity(filmId).get().getLikes().contains(userId)) {
                    throw new EntityExistException("Ошибка уделания лайка," +
                            " пользователь с id = " + userId + " не оценивал фильм.");
                }
                break;
            }
            case "add" : {
                if (filmDao.getEntity(filmId).get().getLikes().contains(userId)) {
                    throw new EntityExistException("Ошибка добавления лайка, пользователь может оценить фильм только один раз");
                }
                break;
            }
        }
    }

    @Override
    public Film addData(@Valid Film film) {

        isValid(film, "add");

        Mpa mpa = mpaDao
                .getById(film.getMpa().getId())
                .orElseThrow(() -> {
                    throw new EntityExistException("Ошибка добавления фильма, значение МРА не найдено.");
                });

        film.setMpa(mpa);

        film.setId(++id);

        return filmDao.addEntity(film);
    }

    @Override
    public Film getData(int id) {

        return filmDao.getEntity(id).orElseThrow(
                () -> {throw new EntityExistException("Ошибка поиска фильма, запись с id =" + id + " не найдена.");}
        );
    }

    @Override
    public Film updateData(@Valid Film film) {

        isValid(film, "with ID");

        Mpa mpa = mpaDao.getById(
                film.getMpa().getId()
                ).orElseThrow(() -> {
                    throw new EntityExistException("Ошибка поиска MPA при обновлении фильма" +
                            ", запись с id =" + film.getMpa().getId() + " не найдена.");
                });

        film.setMpa(mpa);

        return filmDao.updateEntity(film);
    }

    @Override
    public List<Film> getAll() {

        return filmDao.getAll();
    }

    @Override
    public void addLike(int filmId, int userId) {

        isLikeOpValid(userId, filmId, "add");

        filmDao.addLike(filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {

        isLikeOpValid(userId, filmId, "remove");

        filmDao.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {

        return filmDao
                .getAll()
                .stream()
                .sorted(Film::compareTo)
                .limit(count)
                .collect(Collectors.toList());
    }
}
