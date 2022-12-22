package ru.yandex.practicum.filmorate.validation.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.EntityExistException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerValidationTest {

    @Autowired
    private UserController controller;

    private User user1;

    private User user2;

    @BeforeEach
    public void initialize() {

        this.user1 = controller.postUser(new User(
                1,
                "user1mail@mail.ru",
                "admin1",
                "Vasiliy",
                LocalDate.of(2010, 10, 10)
        ));

        this.user2 = controller.postUser(new User(
                2,
                "user2mail@mail.ru",
                "admin2",
                "Vasiliy",
                LocalDate.of(2012, 10, 10)
        ));

    }

    @Test
    void birthdayInFutureTest() {

        user1 = user1
                .toBuilder()
                .birthday(LocalDate.parse("2022-12-30"))
                .build();

        assertThrows(
                InvalidValueException.class,
                () -> controller.postUser(user1)
        );
    }

    @Test
    void userAlreadyExistTest() {

        assertThrows(
                EntityExistException.class,
                () -> controller.postUser(user1)
        );
    }


    @Test
    void updateEntityTest() {

        user1 = user1.toBuilder().login("new Admin").build();

        controller.putUser(user1);

        assertEquals(user1, controller.getUserById(user1.getId()));
    }

    @Test
    void getAllTest() {

        assertEquals(List.of(user1, user2), controller.getUsers());
    }

    @Test
    void addFriendTest() {

        controller.addFriend(user1.getId(), user2.getId());

        assertEquals(List.of(user2), controller.getUserFriends(user1.getId()));
        assertEquals(List.of(), controller.getUserFriends(user2.getId()));
    }

    @Test
    void removeFriendTest() {

        controller.addFriend(user1.getId(), user2.getId());

        controller.deleteFriend(user1.getId(), user2.getId());

        assertEquals(0, controller.getUserFriends(user1.getId()).size());
        assertEquals(0, controller.getUserFriends(user2.getId()).size());
    }

    @Test
    void getMutualFriendsTest() {

        User user3 = new User(
                3,
                "newemmail2@mail.ru",
                "thirdUsr",
                "new Name",
                LocalDate.parse("2000-04-03")
        );

        controller.postUser(user3);

        controller.addFriend(user2.getId(), user1.getId());
        controller.addFriend(user2.getId(), user3.getId());

        controller.addFriend(user1.getId(), user2.getId());

        controller.addFriend(user3.getId(), user2.getId());

        user2.getFriends().add(1);
        user2.getFriends().add(3);

        assertEquals(
                List.of(user2),
                controller.getMutualFriends(user1.getId(), user3.getId())
        );

    }
}