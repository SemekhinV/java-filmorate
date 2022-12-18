package ru.yandex.practicum.filmorate.service.films;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;

public interface FilmService {

    Film addData(Film entity);

    Film getData(int id);

    Film updateData(Film entity);

    List<Film> getAll();

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);
}
