package ru.yandex.practicum.film.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.film.entity.Like;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmDto {

    @Min(value = 0, message = "Id должен быть больше, либо равен нулю.")
    Long id;

    @Positive(message = "Продлжительность должна быть положительной.")
    int duration;

    @NotBlank(message = "Имя фильма не может быть пустым.")
    String name;

    @Length(min = 1, max = 200, message = "Длинна описания не может быть меньше 1 и больше 200 символов.")
    String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate releaseDate;

    List<Like> likes;

    String genre;

    String MPA;
}
