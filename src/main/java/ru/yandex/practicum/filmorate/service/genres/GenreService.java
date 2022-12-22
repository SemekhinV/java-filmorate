package ru.yandex.practicum.filmorate.service.genres;


import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> getAll();

    Genre getById(int id);
}
