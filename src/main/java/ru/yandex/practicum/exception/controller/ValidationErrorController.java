package ru.yandex.practicum.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.exception.EntityExistException;
import ru.yandex.practicum.exception.InvalidValueException;
import ru.yandex.practicum.exception.validation.ErrorResponse;
import ru.yandex.practicum.exception.validation.ValidationErrorResponse;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ValidationErrorController {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse constraintValidationException(
            ConstraintViolationException e
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse methodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<ErrorResponse> violations = e.getBindingResult().getFieldErrors().stream()
                .map(                                               //Создаем объект ErrorResponse
                        //на осове выброшенной ошибки и передаем в нее
                        //Всю необходимую информацию
                        error -> new ErrorResponse(error.getField(), error.getDefaultMessage())
                )
                .collect(Collectors.toList());

        log.error("Ошибка валидации: {}", violations);

        return new ValidationErrorResponse(violations);
    }

    //Обратотка отсутсвия объекта
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> entityExistExceptionHandle(final EntityExistException e) {
        log.error("Ошибка при попытке обращения к объекту: " + e.getMessage());
        return Map.of("Ошибка обращения к объекту: ", e.getMessage());
    }

    //Обработка исключения
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> invalidValueExceptionHandle(final InvalidValueException e) {
        log.error("Ошибка обработки, вызванная некоректными данными: " + e.getMessage());
        return Map.of("Ошибка при обработке некорректных данных: ", e.getMessage());
    }

}
