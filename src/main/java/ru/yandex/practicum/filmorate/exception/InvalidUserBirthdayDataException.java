package ru.yandex.practicum.filmorate.exception;

public class InvalidUserBirthdayDataException extends RuntimeException {
    public InvalidUserBirthdayDataException(String message) {
        super(message);
    }
}
