package ru.yandex.practicum.filmorate.dao.filmgenre;

import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

public interface FilmGenreDao {

    void addGenres(int filmId, List<Integer> genres);

    Film updateFilmGenres(Film film);

    void removeGenres(int filmId, List<Integer> genres);

    List<Genre> getById(int id);
}
