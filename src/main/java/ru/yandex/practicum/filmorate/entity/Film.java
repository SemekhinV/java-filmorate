package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;


@Value                                          //Выбрана аннотация @Value, исходя из логики, что фильм - объект
@Builder(toBuilder = true)                      //Неизменяемый, у него не может поменяться продолжительность или
public class Film {                             //Дата выхода

    @Min(0)
    int id;

    @Positive
    int duration;

    @NotBlank
    String name;

    @Length(min = 1, max = 200)
    String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate releaseDate;
}
