package ru.yandex.practicum.filmorate.validation.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.filmgenre.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.films.FilmDaoImpl;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenreDaoValidationTest {

    @Autowired
    private final FilmDaoImpl filmDao;

    @Autowired
    private final FilmGenreDao filmGenreDao;

    private Film film;

    @BeforeEach
    public void initialize() {
        film = filmDao.addFilm(
                new Film(
                        1,
                        100,
                        "New Film",
                        "new film",
                        LocalDate.of(2012, 12, 12),
                        new Mpa(1, "G")));
    }

    @Test
    void addGenreTest() {

        filmGenreDao.addGenres(film.getId(), List.of(1,2));

        assertEquals(
                List.of(1,2) ,
                filmGenreDao.getById(film.getId())
                        .stream()
                        .map(Genre::getId)
                        .collect(Collectors.toList())
        );
    }

    @Test
    void removeGenreTest() {

        filmGenreDao.addGenres(film.getId(), List.of(1,2));

        filmGenreDao.removeGenres(film.getId(), List.of(1,2));

        assertEquals(0, filmGenreDao.getById(film.getId()).size());
    }

    @Test
    void getGenreTest() {

        filmGenreDao.addGenres(film.getId(), List.of(1));

        assertEquals(new Genre(1, "Комедия"), filmGenreDao.getById(film.getId()).get(0));
    }
}
