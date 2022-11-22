package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.InvalidFilmReleaseDateException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidationTests {

    private FilmController controller;
    private Film film;

    @BeforeEach
    void beforeEach() {
        controller = new FilmController();
        film = Film
                .builder()
                .id(1)
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.parse("1967-03-25"))
                .duration(100)
                .build();
    }

    @Test
    void invalidReleaseDateTest() {
        film = film.toBuilder().releaseDate(LocalDate.parse("1895-12-27")).build();

        assertThrows(
                InvalidFilmReleaseDateException.class,
                () -> controller.postFilm(film)
        );
    }

    @Test
    void filmAlreadyExistsTest() {

        controller.postFilm(film);

        assertThrows(
                EntityAlreadyExistException.class,
                () -> controller.postFilm(film)
        );
    }
}
