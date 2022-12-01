package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.EntityExistException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceValidationTests {

    private UserService service;
    private User user;

    @BeforeEach
    void beforeEach() {
        service = new UserService(new InMemoryUserStorage());
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
        service.addData(user);

        assertThrows(
                EntityExistException.class,
                () -> service.addData(user)
        );
    }

    @Test
    void birthdayInFutureTest() {

        user = user.toBuilder().birthday(LocalDate.parse("2022-12-20")).build();

        assertThrows(
                InvalidValueException.class,
                () -> service.addData(user)
        );
    }

    @Test
    void birthdayInNextCenturyTest() {
        user = user.toBuilder().birthday(LocalDate.parse("2122-12-20")).build();

        assertThrows(
                InvalidValueException.class,
                () -> service.addData(user)
        );
    }

    @Test
    void correctBirthdayTest() {
        user = user.toBuilder().birthday(LocalDate.parse("2002-12-20")).build();

        service.addData(user);

        assertEquals(user, service.getAll().get(1));
    }

    @Test
    void addCorrectDataTest() {

        service.addData(user);

        user.setId(1);

        assertEquals(user, service.getData(1));
    }

    @Test
    void updateEntityTest() {

        service.addData(user);

        User user2 = new User(
                1,
                "newemmail@mail.ru",
                "secondUsr",
                "new Name",
                LocalDate.parse("1999-04-03")
        );

        service.updateData(user2);

        assertEquals(user2, service.getData(user2.getId()));
    }

    @Test
    void removeEntityTest() {

        service.addData(user);

        user.setId(1);

        service.removeData(user);

        assertEquals(0, service.getAll().values().size());
    }

    @Test
    void getAllTest() {

        service.addData(user);

        user.setId(1);

        assertEquals(Map.of(1,user), service.getAll());
    }

    @Test
    void addFriendTest() {

        User user2 = new User(
                1,
                "newemmail@mail.ru",
                "secondUsr",
                "new Name",
                LocalDate.parse("1999-04-03")
        );

        service.addData(user);
        service.addData(user2);

        user.setId(1);
        user2.setId(2);

        service.addFriend(user.getId(), user2.getId());

        assertEquals(List.of(user2), service.getFriends(user.getId()));
        assertEquals(List.of(user), service.getFriends(user2.getId()));
    }

    @Test
    void removeFriendTest() {

        User user2 = new User(
                1,
                "newemmail@mail.ru",
                "secondUsr",
                "new Name",
                LocalDate.parse("1999-04-03")
        );

        service.addData(user);
        service.addData(user2);

        user.setId(1);
        user2.setId(2);

        service.addFriend(user.getId(), user2.getId());

        service.removeFriend(user.getId(), user2.getId());

        assertEquals(0, service.getFriends(user.getId()).size());
        assertEquals(0, service.getFriends(user2.getId()).size());
    }

    @Test
    void getFriendTest() {

        User user2 = new User(
                1,
                "newemmail@mail.ru",
                "secondUsr",
                "new Name",
                LocalDate.parse("1999-04-03")
        );

        service.addData(user);
        service.addData(user2);

        user.setId(1);
        user2.setId(2);

        service.addFriend(user.getId(), user2.getId());

        assertEquals(List.of(user2), service.getFriends(user.getId()));
    }

    @Test
    void getMutualFriendsTest() {

        User user2 = new User(
                1,
                "newemmail@mail.ru",
                "secondUsr",
                "new Name",
                LocalDate.parse("1999-04-03")
        );

        User user3 = new User(
                2,
                "newemmail2@mail.ru",
                "thirdUsr",
                "new Name",
                LocalDate.parse("2000-04-03")
        );

        service.addData(user);
        service.addData(user2);
        service.addData(user3);

        user.setId(1);
        user2.setId(2);
        user.setId(3);

        service.addFriend(user.getId(), user2.getId());     //User(id=1) add friend User(id=2)
        service.addFriend(user3.getId(), user2.getId());     //User(id=3) add friend User(id=2)

        assertEquals(
                List.of(user2),
                service.getMutualFriends(user.getId(), user3.getId())
        );

    }
}
