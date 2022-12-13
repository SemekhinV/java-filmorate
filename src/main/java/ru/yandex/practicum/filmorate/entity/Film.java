package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Value                                          //Выбрана аннотация @Value, исходя из логики, что фильм - объект
@Builder(toBuilder = true)                      //Неизменяемый, у него не может поменяться продолжительность или
public class Film implements Comparable<Film> {                             //Дата выхода

    @Min(
            value = 0,
            message = "Id должен быть больше, либо равен нулю."
    )
    int id;

    @Positive(message = "Продлжительность должна быть положительной.")
    int duration;

    @NotBlank(message = "Имя фильма не может быть пустым.")
    String name;

    @Length(
            min = 1,
            max = 200,
            message = "Длинна описания не может быть меньше 1 и больше 200 символов."
    )
    String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate releaseDate;

    Set<Integer> likes = new HashSet<>();

    @Override                                       //Была добавлена реализация интерфейса Comparable, т.к.
    //При использовании метода Comparable.compareInt()
    public int compareTo(Film film) {               //Вызов метода пропускался и сортировка не выполнялась
        return film.getLikes().size() - this.getLikes().size();
    }

    String genre;

    String MPA;
}
