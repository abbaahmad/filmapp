package com.fip.flexisaf.repositories;

import com.fip.flexisaf.models.Film;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
//@ExtendWith(MockitoExtension.class)
public class FilmRepositoryTest {
    
    @Autowired
    FilmRepository filmRepository;
    
    @AfterEach
    void tearDown(){
        filmRepository.deleteAll();
    }
    @BeforeEach
    void setUp(){
        Film f = new Film().setName("Home Alone");
        filmRepository.save(f);
    }
    @Test
    public void findFilmByNameTest(){
         filmRepository.save(new Film().setName("Con Air"));
         
        Optional<Film> filmConAir = filmRepository.findFilmByName("Con Air");
        
        assertThat(filmConAir).isPresent();
        assertThat(filmConAir.get().getName()).isEqualTo("Con Air");
        
        Optional<Film> nonExistentFilm = filmRepository.findFilmByName("Non-Existent");
        
        assertThat(nonExistentFilm).isEmpty();
    }
    
    @Test
    public void createAndDeleteFilmTest(){
        filmRepository.saveAll(List.of(
                new Film().setName("Avatar"),
                new Film().setName("Pirates"),
                new Film().setName("Maverick"),
                new Film().setName("Con Air"))
        );
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList.size()).isEqualTo(5);
    
        filmRepository.deleteById(3L);
        List<Film> filmList2 = filmRepository.findAll();
        assertThat(filmList2.size()).isEqualTo(4);
    
        filmRepository.deleteAll();
        List<Film> filmList3 = filmRepository.findAll();
        assertThat(filmList3.size()).isEqualTo(0);
    }
}
