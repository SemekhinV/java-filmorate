package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    List<Genre> getAll();

    Optional<Genre> getById(int id);

}
