package com.fip.flexisaf.services.impl;

import com.fip.flexisaf.repositories.FilmRepository;
import com.fip.flexisaf.controllers.requests.film.UpdateFilmRequest;
import com.fip.flexisaf.exceptions.ResourceNotFoundException;
import com.fip.flexisaf.exceptions.ResourceAlreadyExistsException;
import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.controllers.requests.film.FilmRequest;
import com.fip.flexisaf.models.mappers.FilmMapper;
import com.fip.flexisaf.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {
    @Autowired
    FilmRepository filmRepository;
    
    public List<Film> getAll() {
        return filmRepository.findAll();
    }
    
    public Film getOne(Long filmId){
        return filmRepository.findById(filmId)
                             .orElseThrow(() -> new ResourceNotFoundException("Could not find film with ID: " +filmId));
    }
    
    public Film add(FilmRequest filmRequest) {
        
        if (filmRepository.findFilmByName(filmRequest.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException(filmRequest.getName());
        }
        Film film = FilmMapper.toFilm(filmRequest);
        return filmRepository.save(film);
    }
    
    public void delete(Long filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow( () -> {
                                  throw new ResourceNotFoundException("Could not find film with ID: " +filmId);
                              });
        filmRepository.delete(film);
    }
    
    @Transactional
    public Film update(UpdateFilmRequest updateFilmRequest) {
        
        if(filmRepository.findById(updateFilmRequest.getId()).isEmpty()){
            throw new ResourceNotFoundException("Could not find film with ID: " + updateFilmRequest.getId());
        }
        return filmRepository.save(FilmMapper.toFilm(updateFilmRequest));
    }
}
