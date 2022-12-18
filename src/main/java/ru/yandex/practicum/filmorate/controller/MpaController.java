package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaServiceImpl;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaServiceImpl service;

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable int id) {

        log.debug("Get MPA by id {}", id);

        return service.getById(id);
    }

    @GetMapping()
    public List<Mpa> getAll() {

        log.debug("Get all MPA.");

        return service.getAll();
    }
}