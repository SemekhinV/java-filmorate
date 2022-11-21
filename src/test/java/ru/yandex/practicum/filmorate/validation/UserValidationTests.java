package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.InvalidUserBirthdayDataException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTests {

    private UserController controller;
    private User user;

    @BeforeEach
    void beforeEach() {
        controller = new UserController();
        user = User
                .builder()
                .id(0)
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.parse("1946-08-20"))
                .build();
    }

    @Test
    void userAlreadyExistTest() {
        controller.postUser(user);

        assertThrows(
                EntityAlreadyExistException.class,
                () -> controller.postUser(user)
        );
    }

    @Test
    void birthdayInFutureTest() {

        user = user.toBuilder().birthday(LocalDate.parse("2022-12-20")).build();

        assertThrows(
                InvalidUserBirthdayDataException.class,
                () -> controller.postUser(user)
        );
    }

    @Test
    void birthdayInNextCenturyTest() {
        user = user.toBuilder().birthday(LocalDate.parse("2122-12-20")).build();

        assertThrows(
                InvalidUserBirthdayDataException.class,
                () -> controller.postUser(user)
        );
    }

    @Test
    void correctBirthdayTest() {
        user = user.toBuilder().birthday(LocalDate.parse("2002-12-20")).build();

        controller.postUser(user);

        assertEquals(user, controller.getUsers().get(0));
    }
}
