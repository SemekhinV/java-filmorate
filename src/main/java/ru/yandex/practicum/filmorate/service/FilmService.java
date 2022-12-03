package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.EntityExistException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Validated
public class FilmService implements ServicePattern<Film> {

    private static final LocalDate DATE_BOUNDARY_VALUE = LocalDate.of(1895, 12, 28);

    private final InMemoryFilmStorage storage;

    private void isValid(Film film, String flag) {

        if (film.getReleaseDate().isBefore(DATE_BOUNDARY_VALUE)) {
            throw new InvalidValueException("Invalid release date");
        }

        switch (flag) {

            case "add" : {
                if (storage.getAll().containsValue(film)) {

                    throw new EntityExistException("Post error, film " + film.getName() + " already exist.");
                }
                break;
            }
            case "with ID" : {
                if (storage.getAll().isEmpty() || !storage.getAll().containsKey(film.getId())) {
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

        if (!storage.getAll().containsKey(filmId)) {
            throw new EntityExistException("Ошибка добавления лайка фильму, фильм с id = " + filmId + " не найден.");
        }

        switch (flag) {
            case "remove" : {
                if (!storage.getEntity(filmId).getLikes().contains(userId)) {
                    throw new EntityExistException("Ошибка уделания лайка," +
                            " пользователь с id = " + userId + " не оценивал фильм.");
                }
                break;
            }
            case "add" : {
                if (storage.getEntity(filmId).getLikes().contains(userId)) {
                    throw new EntityExistException("Ошибка добавления лайка, пользователь может оценить фильм только один раз");
                }
                break;
            }
        }
    }

    @Override
    public Film addData(@Valid Film film) {

        isValid(film, "add");

        return storage.addEntity(film);
    }

    @Override
    public Film getData(int id) {

        if (storage.getAll().isEmpty() || !storage.getAll().containsKey(id)) {
            throw new EntityExistException("Ошибка поиска фильма, запись с id = " + id + " не надена.");
        }

        return storage.getEntity(id);
    }

    @Override
    public Film updateData(@Valid Film film) {

        isValid(film, "with ID");

        return storage.updateEntity(film);
    }

    @Override
    public Film removeData(@Valid Film film) {

        isValid(film, "with ID");

        return storage.removeEntity(film);
    }

    @Override
    public Map<Integer, Film> getAll() {

        return storage.getAll();
    }

    public void addLike(int filmId, int userId) {

        isLikeOpValid(userId, filmId, "add");

        storage.getEntity(filmId).getLikes().add(userId);
    }

    public void removeLike(int filmId, int userId) {

        isLikeOpValid(userId, filmId, "remove");

        storage.getEntity(filmId).getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {

        return storage
                .getAll()
                .values()
                .stream()
                .sorted(Film::compareTo)
                .limit(count)
                .collect(Collectors.toList());
    }
}