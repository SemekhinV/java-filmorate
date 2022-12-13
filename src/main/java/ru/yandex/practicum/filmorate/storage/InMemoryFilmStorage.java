package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Film;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements Storage<Film> {

    @NotNull
    private final Map<Integer, Film> filmStorage;

    private Integer id;

    public InMemoryFilmStorage() {

        this.filmStorage = new HashMap<>();
        this.id = 0;
    }

    @Override
    public Film addEntity(Film film) {

        film = film.toBuilder().id(++id).build();
        filmStorage.put(film.getId(), film);

        return film;
    }

    @Override
    public Film getEntity(int id) {
        return filmStorage.get(id);
    }

    @Override
    public Film removeEntity(Film film) {
        return filmStorage.remove(film.getId());
    }

    @Override
    public Film updateEntity(Film film) {
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Map<Integer, Film> getAll() {
        return filmStorage;
    }

}
