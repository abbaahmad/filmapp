package com.fip.flexisaf.controllers.api;

import com.fip.flexisaf.controllers.requests.film.UpdateFilmRequest;
import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.controllers.requests.film.FilmRequest;
import com.fip.flexisaf.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path="api/v1/film")
public class FilmController {
    
    @Autowired
    FilmService filmService;
    
    @GetMapping("/all")
    public List<Film> getAll(){
        return filmService.getAll();
    }
    
    @GetMapping("/{filmId}")
    public Film getOne(@PathVariable Long filmId){
        return filmService.getOne(filmId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film add(@Valid @RequestBody FilmRequest filmRequest, @AuthenticationPrincipal UserDetails userDetails){
        return filmService.add(filmRequest, userDetails);
    }
    
    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable Long filmId, @AuthenticationPrincipal UserDetails userDetails){
        filmService.delete(filmId, userDetails);
    }
    
    @PutMapping()
    public Film update(@Valid @RequestBody UpdateFilmRequest updateFilmRequest, @AuthenticationPrincipal UserDetails userDetails){
        return filmService.update(updateFilmRequest, userDetails);
    }
}
