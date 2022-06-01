package com.fip.flexisaf.services;


import com.fip.flexisaf.controllers.requests.film.UpdateFilmRequest;
import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.controllers.requests.film.FilmRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface FilmService {
    List<Film> getAll();
    Film getOne(Long filmId);
    Film add(FilmRequest filmRequest, UserDetails userDetails);
    void delete(Long filmId, UserDetails userDetails);
    Film update(UpdateFilmRequest updateFilmRequest, UserDetails userDetails);
}
