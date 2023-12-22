//package ru.yandex.practicum.filmorate.validation;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.film.entity.Film;
//import ru.yandex.practicum.filmorate.exception.EntityExistException;
//import ru.yandex.practicum.filmorate.exception.InvalidValueException;
//import ru.yandex.practicum.filmorate.service.impl.FilmServiceImpl;
//import ru.yandex.practicum.filmorate.repository.impl.InMemoryFilmStorageImpl;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class FilmServiceValidationTests {
//
//    private FilmServiceImpl service;
//    private Film film;
//
//    @BeforeEach
//    void beforeEach() {
//
//        service = new FilmServiceImpl(new InMemoryFilmStorageImpl());
//
//        film = Film
//                .builder()
//                .id(0)
//                .name("nisi eiusmod")
//                .description("adipisicing")
//                .releaseDate(LocalDate.parse("1967-03-25"))
//                .duration(100)
//                .build();
//    }
//
//    @Test
//    void invalidReleaseDateTest() {
//        film = film.toBuilder().releaseDate(LocalDate.parse("1895-12-27")).build();
//
//        assertThrows(
//                InvalidValueException.class,
//                () -> service.addData(film)
//        );
//    }
//
//    @Test
//    void filmAlreadyExistsTest() {
//
//        service.addData(film);
//
//        assertThrows(
//                EntityExistException.class,
//                () -> service.addData(film.toBuilder().id(1).build())
//        );
//    }
//
//    @Test
//    void addCorrectDataTest() {
//        service.addData(film);
//
//        assertEquals(film.toBuilder().id(1).build(), service.getData(1));
//    }
//
//    @Test
//    void updateEntityTest() {
//
//        service.addData(film);
//
//        film = film.toBuilder()
//                .duration(400)
//                .description("new Film")
//                .id(1)
//                .name("updatedFilm")
//                .build();
//
//        service.updateData(film);
//
//        assertEquals(film, service.getData(film.getId()));
//    }
//
//    @Test
//    void removeEntityTest() {
//
//        service.addData(film);
//
//        service.removeData(film.toBuilder().id(1).build());
//
//        assertEquals(0, service.getAll().values().size());
//    }
//
//    @Test
//    void getAllTest() {
//
//        service.addData(film);
//
//        assertEquals(Map.of(1, film.toBuilder().id(1).build()), service.getAll());
//    }
//
//    @Test
//    void addLikeTest() {
//
//        service.addData(film);
//
//        service.addLike(film.toBuilder().id(1).build().getId(), 1);
//
//        assertEquals(
//                1,
//                service.getData(film.getId()+1).getLikes().size()
//        );
//    }
//
//    @Test
//    void removeLikeTest() {
//
//        service.addData(film);
//
//        service.addLike(film.toBuilder().id(1).build().getId(), 1);
//
//        service.removeLike(film.toBuilder().id(1).build().getId(), 1);
//
//        assertEquals(0, service.getData(film.getId()+1).getLikes().size());
//    }
//
//    @Test
//    void getPopularFilmTest() {
//
//        Film film2 = film.toBuilder()
//                .id(1)
//                .description("Second Film")
//                .name("secondFilm")
//                .duration(123)
//                .releaseDate(LocalDate.parse("1977-04-03"))
//                .build();
//
//        service.addData(film);
//        service.addData(film2);
//
//        film = film.toBuilder().id(1).build();
//        film2 = film2.toBuilder().id(2).build();
//
//        service.addLike(film.getId(), 1);
//
//        service.addLike(film2.getId(), 1);
//        service.addLike(film2.getId(), 2);
//
//        film.getLikes().add(1);
//
//        film2.getLikes().addAll(Set.of(1,2));
//
//        assertEquals(
//                List.of(film2, film),
//                service.getPopularFilms(2)
//        );
//    }
//
//}
