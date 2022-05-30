package com.fip.flexisaf.controllers.api;

import com.fip.flexisaf.controllers.requests.film.UpdateFilmRequest;
import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.controllers.requests.film.FilmRequest;
import com.fip.flexisaf.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Film add(@Valid @RequestBody FilmRequest filmRequest){
        return filmService.add(filmRequest);
    }
    
    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable Long filmId){
        filmService.delete(filmId);
    }
    
    @PutMapping()
    public Film update(@Valid @RequestBody UpdateFilmRequest updateFilmRequest){
        return filmService.update(updateFilmRequest);
    }
}
