package ru.yandex.practicum.filmorate.exception;

public class InvalidFilmReleaseDateException extends RuntimeException{
    public InvalidFilmReleaseDateException(String message) {
        super(message);
    }
}
