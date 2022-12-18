package ru.yandex.practicum.filmorate.dao.films;

import ru.yandex.practicum.filmorate.entity.Film;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;



public class InMemoryFilmStorage{

    @NotNull
    private final Map<Integer, Film> filmStorage;

    private Integer id;

    public InMemoryFilmStorage() {

        this.filmStorage = new HashMap<>();
        this.id = 0;
    }


    public Film addEntity(Film film) {

        film = film.toBuilder().id(++id).build();
        filmStorage.put(film.getId(), film);

        return film;
    }


    public Optional<Film> getEntity(int id) {
        return Optional.ofNullable(filmStorage.get(id));
    }


    public void addLike(int film, int userId) {

    }


    public void deleteLike(int film, int userId) {

    }


    public List<Film> getPopularFilms(int count) {
        return null;
    }




    public Film updateEntity(Film film) {
        filmStorage.put(film.getId(), film);
        return film;
    }


    public List<Film> getAll() {
        //return filmStorage;
        return List.of();
    }

}
