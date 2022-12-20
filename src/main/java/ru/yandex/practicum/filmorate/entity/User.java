package ru.yandex.practicum.filmorate.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class User {

    @Min(
            value = 0,
            message = "Значение поля id должно быть больше нуля"
    )
    int id;

    @Email(message = "Некорректный вид email-адреса.")
    String email;

    @NotBlank
    @Pattern(
            regexp = "^\\S*$",
            message = "Поле login не долно содержать пробелов."
    )                           //Регулярное выражение для проверки отсутсвия пробелов
    String login;

    String name;

    @PastOrPresent              //Аннотация указывает на прошлое, включая настоящее
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    LocalDate birthday;

    final Set<Integer> friends = new HashSet<>();
}
