package com.fip.flexisaf.services;

import com.fip.flexisaf.models.Role;
import com.fip.flexisaf.models.User;
import com.fip.flexisaf.repositories.FilmRepository;
import com.fip.flexisaf.controllers.requests.film.FilmRequest;
import com.fip.flexisaf.controllers.requests.film.UpdateFilmRequest;
import com.fip.flexisaf.exceptions.ResourceNotFoundException;
import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.models.mappers.FilmMapper;
import com.fip.flexisaf.repositories.UserRepository;
import com.fip.flexisaf.services.impl.FilmServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {
    @Mock
    FilmRepository filmRepository;
    
    @Mock
    UserRepository userRepository;
    
    @Autowired
    @InjectMocks
    FilmServiceImpl filmService;
    
    @Test
    public void addFilmTest(){
        FilmRequest filmRequest = getFilm();
        Film film = FilmMapper.toFilm(filmRequest);
        
        when(filmRepository.findFilmByName(any(String.class))).thenReturn(Optional.empty());
        when(filmRepository.save(any(Film.class))).thenReturn(film);
    
        User bob = new User()
                .setEmail("bobreed@cbt.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.ADMINISTRATOR);
    
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
    
    
        Film newFilm = filmService.add(filmRequest, bob);
        assertThat(newFilm).isNotNull();
        assertThat(newFilm.getName()).isEqualTo(film.getName());
    }
    
    @Test
    public void getOneTest(){
        Long id = 1L;
    
        User bob = new User()
                .setEmail("bobreed@cbt.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.ADMINISTRATOR);
    
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
    
        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
            filmService.delete(id, bob);
        });
    
        assertEquals(e.getMessage(), "Could not find film with ID: " + id);
    
        when(filmRepository.findById(any(Long.class))).thenReturn(Optional.of(new Film()));
    
        Film film = filmService.getOne(id);
    
        assertThat(film).isNotNull();
    }
    
    @Test
    public void getAllTest(){
        when(filmRepository.findAll()).thenReturn(List.of(new Film(), new Film()));
        
        List<Film> filmList = filmService.getAll();
        assertThat(filmList.size()).isEqualTo(2);
    }
    
    @Test
    public void deleteTest(){
        Long id = 1L;
    
        User bob = new User()
                .setEmail("bobreed@cbt.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.ADMINISTRATOR);
    
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
    
        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
            filmService.delete(id, bob);
        });
    
        assertEquals(e.getMessage(), "Could not find film with ID: " + id);
    
        when(filmRepository.findById(any(Long.class))).thenReturn(Optional.of(new Film()));
    
        filmService.delete(id, bob);
    
        verify(filmRepository, times(1)).delete(any(Film.class));
    }
    
    @Test
    public void updateTest(){
        Long id = 2L;
    
        User bob = new User()
                .setEmail("bobreed@cbt.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.ADMINISTRATOR);
    
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
    
        when(filmRepository.findById(any(Long.class))).thenReturn(Optional.of(new Film()));
    
        UpdateFilmRequest updateFilmRequest = new UpdateFilmRequest()
                .setId(id);
        when(filmRepository.save(any(Film.class))).thenReturn(FilmMapper.toFilm(updateFilmRequest));
    
        Film film = filmService.update(updateFilmRequest, bob);
        assertEquals(film.getId(), id);
    }
    
    FilmRequest getFilm(){
        return new FilmRequest()
                .setDescription("Film description")
                .setRating(9.0)
                .setGenre("film genre")
                .setName("Film name")
                .setReleaseDate(LocalDate.parse("2022-05-27"));
    }
}
