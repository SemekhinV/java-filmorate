package ru.yandex.practicum.filmorate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class User {

    @Min(
            value = 0,
            message = "Значение поля id должно быть больше нуля"
    )
    private int id;

    @Email(message = "Некорректный вид email-адреса.")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^\\S*$",
            message = "Поле login не долно содержать пробелов."
    )                           //Регулярное выражение для проверки отсутсвия пробелов
    private String login;

    private String name;

    @PastOrPresent              //Аннотация указывает на прошлое, включая настоящее
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;

    private final Set<Integer> friends = new HashSet<>();
}
