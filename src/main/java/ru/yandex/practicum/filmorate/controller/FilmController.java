package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.InvalidFilmReleaseDateException;
import ru.yandex.practicum.filmorate.entity.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final HashMap<Integer, Film> filmList;
    private int id;
    private static final LocalDate DATE_BOUNDARY_VALUE = LocalDate.ofYearDay(1895, 273);

    public FilmController() {
        this.filmList = new HashMap<>();
        this.id = 0;
    }

    @PostMapping()
    public Film postFilm(@Valid @RequestBody Film film) {

        if (film.getReleaseDate().isBefore(DATE_BOUNDARY_VALUE)) {
            log.error("Invalid release date");
            throw new InvalidFilmReleaseDateException("Invalid release date");
        }

        if (filmList.containsKey(film.getId())) {
            log.error("Film {} already exist.", film.getName());
            log.debug("Film AE data: {}", film);
            throw new EntityAlreadyExistException("Post error, film " + film.getName() + " already exist.");
        }

        log.info("Post new film - {}", film.getName());                 //Логирование
        log.debug("Post film data: {}", film);

        film = film.toBuilder().id(++id).build();

        filmList.put(film.getId(), film);
        return film;
    }

    @PutMapping()
    public Film putFilm(@Valid @RequestBody Film film) {

        if (film.getReleaseDate().isBefore(DATE_BOUNDARY_VALUE)) {
            throw new InvalidFilmReleaseDateException("Invalid release date");
        }

        if (!filmList.containsKey(film.getId())) {

            log.error("Put error, film {} does`t exist", film.getName());
            log.debug("Film put error data: {}", film);
            throw new EntityAlreadyExistException("Put error, film " + film.getName() + " does`t exist.");
        }

        log.info("Put new film - {}", film.getName());                 //Логирование
        log.debug("Put film data: {}", film);

        filmList.put(film.getId(), film);

        return film;
    }

    @GetMapping
    public ArrayList<Film> getFilms() {
        return new ArrayList<>(filmList.values());
    }
}
