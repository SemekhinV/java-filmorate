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

    private static final LocalDate DATE_BOUNDARY_VALUE = LocalDate.ofYearDay(1895, 362);

    private final InMemoryFilmStorage storage;

    @Override
    public Film addData(@Valid Film film) {

        if (film.getReleaseDate().isBefore(DATE_BOUNDARY_VALUE)) {
            throw new InvalidValueException("Invalid release date");
        }

        if (storage.getAll().containsValue(film)) {

            throw new EntityExistException("Post error, film " + film.getName() + " already exist.");
        }

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

        if (film.getReleaseDate().isBefore(DATE_BOUNDARY_VALUE)) {
            throw new InvalidValueException("Invalid release date");
        }

        if (storage.getAll().isEmpty() || !storage.getAll().containsKey(film.getId())) {
            throw new EntityExistException("Ошибка обновления фильма, запись с id = "
                    + film.getId() + " не существует.");
        }

        return storage.updateEntity(film);
    }

    @Override
    public Film removeData(@Valid Film film) {

        if (film.getReleaseDate().isBefore(DATE_BOUNDARY_VALUE)) {
            throw new InvalidValueException("Invalid release date");
        }

        if (storage.getAll().isEmpty() || !storage.getAll().containsKey(film.getId())) {
            throw new EntityExistException("Ошибка удаления фильма, записи с id = "
                    + film.getId() + " не существует.");
        }

        return storage.removeEntity(film);
    }

    @Override
    public Map<Integer, Film> getAll() {

        return storage.getAll();
    }

    public void addLike(int filmId, int userId) {

        if (userId < 0 || userId > 1000) {
            throw new InvalidValueException("Ошибка добавление лайка, некорректный ID.");
        }

        if (!storage.getAll().containsKey(filmId)) {
            throw new EntityExistException("Ошибка добавления лайка фильму, фильм с id = " + filmId + " не найден.");
        }

        if (storage.getEntity(filmId).getLikes().contains(userId)) {
            throw new EntityExistException("Ошибка добавления лайка, пользователь может оценить фильм только один раз");
        }

        storage.getEntity(filmId).getLikes().add(userId);
    }

    public void removeLike(int filmId, int userId) {

        if (userId < 0 || userId > 1000) {
            throw new InvalidValueException("Ошибка добавление лайка, некорректный ID.");
        }

        if (!storage.getAll().containsKey(filmId)) {
            throw new EntityExistException("Ошибка добавления лайка фильму, фильм с id = " + filmId + " не найден.");
        }

        if (!storage.getEntity(filmId).getLikes().contains(userId)) {
            throw new EntityExistException("Ошибка уделания лайка," +
                    " пользователь с id = " + userId + " не оценивал фильм.");
        }

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
