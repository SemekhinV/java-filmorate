package ru.yandex.practicum.filmorate.dao.filmlikes;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

public interface FilmLikesDao {


    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Integer> getUserLikes(int filmId);
}
