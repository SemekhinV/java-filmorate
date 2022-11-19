package ru.yandex.practicum.filmorate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {


    @Min(0)
    private int id;

    @Email
    private String email;       //Пробная версия валидации email-адреса. В нем разрешено
                                //1. числа от 0 до 9/2. Символы всех регистров (английские)/3. Знаки "-" "_" "."
                                //4. Запрещено две точки подряд и точки в начале и в конце
                                //5. Длинна части до знака @ не более 64 символов и не меньше 1, можно изменить
    @NotBlank
    @Pattern(regexp = "^\\S*$") //Регулярное выражение для проверки отсутсвия пробелов
    private String login;

    private String name;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;

}
