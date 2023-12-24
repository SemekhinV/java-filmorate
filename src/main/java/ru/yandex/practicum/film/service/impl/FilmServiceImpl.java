package ru.yandex.practicum.film.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.exception.EntityExistException;
import ru.yandex.practicum.film.dto.FilmDto;
import ru.yandex.practicum.film.entity.Like;
import ru.yandex.practicum.film.mapepr.FilmMapper;
import ru.yandex.practicum.film.repository.FilmorateRepository;
import ru.yandex.practicum.film.repository.LikeRepository;
import ru.yandex.practicum.film.service.FilmService;
import ru.yandex.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmMapper mapper;

    private final FilmorateRepository repository;

    private final UserRepository userRepository;

    private final LikeRepository likeRepository;

    @Override
    public FilmDto save(FilmDto dto) {

        var film = repository.save(mapper.toEntity(dto));

        return mapper.toDto(film);
    }

    @Override
    public FilmDto getById(Long id) {

        var film = repository.findById(id).orElseThrow(
                () -> {
                    throw new EntityExistException("Entity with id = " + id + "does`t exist");
                });

        var response = mapper.toDto(film);

        response.addLike(likeRepository.findAllByFilmId(id));

        return response;
    }

    @Override
    public FilmDto update(Long id, FilmDto dto) {

        var fromDb = repository.findById(id).orElseThrow(
                () -> {
                    throw new EntityExistException("Entity with id = " + id + "does`t exist");
                });

        if (dto.getDescription() != null) {

            fromDb.setDescription(dto.getDescription());
        }

        if (dto.getName() != null) {

            fromDb.setName(dto.getName());
        }

        if (dto.getDescription() != null) {

            fromDb.setDescription(dto.getDescription());
        }

        if (dto.getReleaseDate() != null) {

            fromDb.setReleaseDate(dto.getReleaseDate());
        }

        if (dto.getGenre() != null) {

            fromDb.setGenre(dto.getGenre());
        }

        if (dto.getMPA() != null) {

            fromDb.setMPA(dto.getMPA());
        }

        return mapper.toDto(repository.save(fromDb));
    }

    @Override
    public void delete(Long id) {

        repository.findById(id).orElseThrow(
                () -> {
                    throw new EntityExistException("Entity with id = " + id + "does`t exist");
                });

        repository.deleteById(id);
    }

    @Override
    public FilmDto addLike(Long filmId, Long userId) {

        var user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new EntityExistException("Entity with id = " + userId + "does`t exist");
                });

        var film = repository.findById(filmId).orElseThrow(
                () -> {
                    throw new EntityExistException("Entity with id = " + filmId + "does`t exist");
                });

        likeRepository.save(new Like(null, film, user));

        var response = mapper.toDto(film);

        response.setLikes(likeRepository.findAllByFilmId(filmId));

        return response;
    }

    @Override
    public void removeLike(Long filmId, Long userId) {

        userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new EntityExistException("Entity with id = " + userId + "does`t exist");
                });

        repository.findById(filmId).orElseThrow(
                () -> {
                    throw new EntityExistException("Entity with id = " + filmId + "does`t exist");
                });

        var likes = likeRepository.findByFilmIdAndUserId(filmId, userId);

        if (likes.isEmpty()) {

            return;
        }

        likeRepository.deleteAllById(likes.stream().map(Like::getId).toList());
    }

    @Override
    public List<FilmDto> getPopularFilms(int count) {

        var likes = likeRepository.getByRating(PageRequest.of(1, count));

        return mapper.toDtoList(repository.findAllById(likes));
    }
}
