package ru.yandex.practicum.filmorate.dao.films;

import ru.yandex.practicum.filmorate.entity.Film;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface FilmDao {
                                            //Удобнее иметь один интерфейс под хранилище, чтобы не
    Film addFilm(@Valid Film film);       //Захламлять код лишними классами, т.к.

                                            //При обе реализации имеют одинаковые методы
    Optional<Film> getFilm(int id);

    List<Film> getPopularFilms(int count);

    List<Film> getAll();

    Film updateFilm(@Valid Film film);
}
