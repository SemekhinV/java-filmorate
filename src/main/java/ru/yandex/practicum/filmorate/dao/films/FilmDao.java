package ru.yandex.practicum.filmorate.dao.films;

import ru.yandex.practicum.filmorate.entity.Film;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface FilmDao {
                                            //Удобнее иметь один интерфейс под хранилище, чтобы не
    Film addEntity(@Valid Film entity);     //Захламлять код лишними классами, т.к.

                                            //При обе реализации имеют одинаковые методы
    Optional<Film> getEntity(int id);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);

    List<Film> getAll();

    Film updateEntity(@Valid Film entity);
}
