package ru.yandex.practicum.film.service;

import ru.yandex.practicum.film.dto.FilmDto;

import java.util.List;

public interface FilmService {

    FilmDto save(FilmDto entity);

    FilmDto getById(Long id);

    void delete(Long id);

    FilmDto update(Long id, FilmDto film);

    FilmDto addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<FilmDto> getPopularFilms(int count);
}
