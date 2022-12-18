package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.service.genres.GenreService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {

        log.debug("Get genre by id {}", id);

        return service.getById(id);
    }

    @GetMapping()
    public List<Genre> getAll() {

        log.debug("Get all genres.");

        return service.getAll();
    }
}

