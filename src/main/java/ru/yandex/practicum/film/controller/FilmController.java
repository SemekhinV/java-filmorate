package ru.yandex.practicum.film.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.film.dto.FilmDto;
import ru.yandex.practicum.film.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto save(@Valid @RequestBody FilmDto film) {

        log.info("Post new film - {}", film);

        return filmService.save(film);
    }

    @PutMapping("/{filmId}")
    public FilmDto update(@Valid @RequestBody FilmDto film, @PathVariable Long filmId) {

        log.info("Put new film - {}", film);

        return filmService.update(filmId, film);
    }

    @GetMapping("/{filmId}")
    public FilmDto getById(@PathVariable Long filmId) {

        log.info("Запрос на получение данных о фильме с id = {}", filmId);

        return filmService.getById(filmId);
    }

    @DeleteMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long filmId) {

        log.info("Удаление фильма filmId = {}", filmId);

        filmService.delete(filmId);
    }

    @GetMapping("/popular/{count}")
    public List<FilmDto> getPopularFilms(@PathVariable int count) {

        return filmService.getPopularFilms(count);
    }

    @PostMapping("/{filmId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto addLike(@PathVariable Long filmId, @PathVariable Long userId) {

        log.info("add new like - {}{}", filmId, userId);

        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {

        log.info("Удаление лайка filmId = {}", filmId);

        filmService.removeLike(filmId, userId);
    }
}
