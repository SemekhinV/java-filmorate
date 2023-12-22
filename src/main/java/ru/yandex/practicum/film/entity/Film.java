package ru.yandex.practicum.film.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@Table(name = "films")
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    Long duration;

    @Column
    String name;

    @Column
    String description;

    @Column
    LocalDate releaseDate;

    @Column
    String genre;

    @Column
    String MPA;
}
