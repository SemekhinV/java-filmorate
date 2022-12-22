package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.service.films.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@Validated
public class FilmController {

    private final FilmService service;

    @PostMapping()
    public Film postFilm(@RequestBody @Valid Film film) {

        log.info("Post new film - {}", film);                 //Логирование

        return service.addFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {

        log.info("Запрос на получение данных фильма с id = {}", filmId);
        return service.getFilm(filmId);
    }

    @PutMapping()
    public Film putFilm(@RequestBody @Valid Film film) {

        log.info("Put new film - {}", film);                 //Логирование

        return service.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return service.getAll();
    }

    @PutMapping("{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable("id") int filmId, @PathVariable int userId) {

        service.addLike(filmId, userId);

        log.info("Фильму id = {} добавлен лайк от пользователя userId = {}", filmId, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLikeToFilm(@PathVariable("id") int filmId, @PathVariable int userId) {

        service.removeLike(filmId, userId);

        log.info("Фильму id = {} был удален лайк от пользователя userId = {}", filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilm(@RequestParam(defaultValue = "10") int count) {

        log.info("Был выведен список {} наиболее популярных фильмов", count);

        return service.getPopularFilms(count);
    }


}
