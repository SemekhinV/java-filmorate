package ru.yandex.practicum.film.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.film.entity.Like;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findAllByFilmId(Long filmId);

    List<Like> findByFilmIdAndUserId(Long filmId, Long userId);

    @Query("SELECT l.film.id " +
            "FROM Like AS l " +
            "ORDER BY COUNT(l.film.id) DESC")
    Page<Long> getByRating(Pageable pageable);
}