package ru.yandex.practicum.filmorate.service.films;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;

public interface FilmService {

    Film addFilm(Film film);

    Film getFilm(int id);

    Film updateFilm(Film film);

    List<Film> getAll();

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);
}
