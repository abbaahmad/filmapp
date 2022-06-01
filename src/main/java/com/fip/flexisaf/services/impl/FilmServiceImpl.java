package com.fip.flexisaf.services.impl;

import com.fip.flexisaf.models.Role;
import com.fip.flexisaf.models.User;
import com.fip.flexisaf.repositories.FilmRepository;
import com.fip.flexisaf.controllers.requests.film.UpdateFilmRequest;
import com.fip.flexisaf.exceptions.ResourceNotFoundException;
import com.fip.flexisaf.exceptions.ResourceAlreadyExistsException;
import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.controllers.requests.film.FilmRequest;
import com.fip.flexisaf.models.mappers.FilmMapper;
import com.fip.flexisaf.repositories.UserRepository;
import com.fip.flexisaf.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {
    @Autowired
    FilmRepository filmRepository;
    
    @Autowired
    UserRepository userRepository;
    
    public List<Film> getAll() {
        return filmRepository.findAll();
    }
    
    public Film getOne(Long filmId){
        return filmRepository.findById(filmId)
                             .orElseThrow(() -> new ResourceNotFoundException("Could not find film with ID: " +filmId));
    }
    
    public Film add(FilmRequest filmRequest, UserDetails userDetails) {
       User user = userRepository.findUserByEmail(userDetails.getUsername())
               .orElseThrow(() -> {
                   throw new ResourceNotFoundException("No such User "+userDetails.getUsername());
               });
       if(!user.getRole().equals(Role.ADMINISTRATOR))
           throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credentials unauthorised for such action");
       
        if (filmRepository.findFilmByName(filmRequest.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException(filmRequest.getName());
        }
        Film film = FilmMapper.toFilm(filmRequest);
        return filmRepository.save(film);
    }
    
    public void delete(Long filmId, UserDetails userDetails) {
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                                  .orElseThrow(() -> {
                                      throw new ResourceNotFoundException("No such User "+userDetails.getUsername());
                                  });
        if(!user.getRole().equals(Role.ADMINISTRATOR))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credentials unauthorised for such action");
    
        Film film = filmRepository.findById(filmId)
                .orElseThrow( () -> {
                                  throw new ResourceNotFoundException("Could not find film with ID: " +filmId);
                              });
        filmRepository.delete(film);
    }
    
    @Transactional
    public Film update(UpdateFilmRequest updateFilmRequest, UserDetails userDetails) {
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                                  .orElseThrow(() -> {
                                      throw new ResourceNotFoundException("No such User "+userDetails.getUsername());
                                  });
        if(!user.getRole().equals(Role.ADMINISTRATOR))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credentials unauthorised for such action");
    
        if(filmRepository.findById(updateFilmRequest.getId()).isEmpty()){
            throw new ResourceNotFoundException("Could not find film with ID: " + updateFilmRequest.getId());
        }
        return filmRepository.save(FilmMapper.toFilm(updateFilmRequest));
    }
}
