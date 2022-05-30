package com.fip.flexisaf.services;


import com.fip.flexisaf.controllers.requests.film.UpdateFilmRequest;
import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.controllers.requests.film.FilmRequest;

import java.util.List;

public interface FilmService {
    List<Film> getAll();
    Film getOne(Long filmId);
    Film add(FilmRequest filmRequest);
    void delete(Long filmId);
    Film update(UpdateFilmRequest updateFilmRequest);
}
