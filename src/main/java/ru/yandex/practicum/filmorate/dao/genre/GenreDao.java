package ru.yandex.practicum.filmorate.dao.genre;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;

public interface GenreDao {

    List<Genre> getAll();

    Optional<Genre> getById(int id);
}
