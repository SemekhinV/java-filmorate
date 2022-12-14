package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityExistException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.exception.validation.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.validation.ValidationErrorResponse;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ValidationErrorController {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse constraintValidationException(
            final ConstraintViolationException e
    ) {
        final List<ErrorResponse> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new ErrorResponse(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());

        log.error("Ошибка валидации: {}", violations);

        return new ValidationErrorResponse(violations);
    }

    //Обратотка отсутсвия объекта
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityExistExceptionHandle(final EntityExistException e) {
        log.error("Ошибка при попытке обращения к объекту: " + e.getMessage());
        return new ErrorResponse("Ошибка обращения к объекту: ".concat(e.getMessage()));
    }

    //Обработка исключения
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidValueExceptionHandle(final InvalidValueException e) {
        log.error("Ошибка обработки, вызванная некоректными данными: " + e.getMessage());
        return new ErrorResponse("Ошибка при обработке некорректных данных: ".concat(e.getMessage()));
    }

}
