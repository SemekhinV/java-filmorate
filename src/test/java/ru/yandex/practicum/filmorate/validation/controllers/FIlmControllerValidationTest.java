package ru.yandex.practicum.filmorate.validation.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.EntityExistException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FIlmControllerValidationTest {

    @Autowired
    private FilmController controller;

    @Autowired
    private UserController service;

    private Film film;
    private User user;

    @BeforeEach
    public void initialize() {

        this.film = controller.postFilm(new Film(
                0,
                100,
                "Film",
                "New Film",
                LocalDate.now(),
                new Mpa(1, "G")
        ));

        this.user = service.postUser(new User(
                1,
                "usermail@mail.ru",
                "admin",
                "Vasiliy",
                LocalDate.of(2010, 10, 10)
        ));
    }

    @Test
    void addSecondFilm() {

        Film film2 = new Film(
                0,
                100,
                "Film2",
                "New Film2",
                LocalDate.now(),
                new Mpa(1, "G")
        );

        controller.postFilm(film2);

        assertEquals(film2, controller.getFilmById(2));

    }

    @Test
    void invalidReleaseDateTest() {

        film = film.toBuilder().releaseDate(LocalDate.parse("1895-12-27")).build();

        assertThrows(
                InvalidValueException.class,
                () -> controller.postFilm(film)
        );
    }

    @Test
    void filmAlreadyExistsTest() {

        assertThrows(
                EntityExistException.class,
                () -> controller.postFilm(film)
        );
    }

    @Test
    void updateEntityTest() {

        film = film.toBuilder()
                .id(1)
                .duration(400)
                .description("new Film")
                .name("updatedFilm")
                .build();

        controller.putFilm(film);

        assertEquals(film, controller.getFilmById(film.getId()));
    }

    @Test
    void getAllTest() {

        assertEquals(List.of(film), controller.getFilms());
    }

    @Test
    void addLikeTest() {

        controller.addLikeToFilm(film.getId(), user.getId());

        assertEquals(
                1,
                controller.getFilmById(film.getId()).getLikes().size()
        );
    }

    @Test
    void removeLikeTest() {

        controller.addLikeToFilm(film.getId(), user.getId());

        controller.removeLikeToFilm(film.getId(), user.getId());

        assertEquals(0, controller.getFilmById(film.getId()).getLikes().size());
    }

    @Test
    void getPopularFilmTest() {

        Film film2 = film.toBuilder()
                .id(2)
                .description("Second Film")
                .name("secondFilm")
                .duration(123)
                .releaseDate(LocalDate.parse("1977-04-03"))
                .build();

        User user2 = user.toBuilder().id(2).build();

        controller.postFilm(film2);

        service.postUser(user2);

        controller.addLikeToFilm(film.getId(), user.getId());

        controller.addLikeToFilm(film2.getId(), user.getId());
        controller.addLikeToFilm(film2.getId(), user2.getId());

        film.getLikes().add(1);

        film2.getLikes().addAll(Set.of(1,2));

        assertEquals(
                List.of(film2, film),
                controller.getPopularFilm(2)
        );
    }
}
