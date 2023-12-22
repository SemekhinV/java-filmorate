package ru.yandex.practicum.film.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.film.entity.Film;

@Repository
public interface FilmorateRepository extends JpaRepository<Film, Long> {

}
