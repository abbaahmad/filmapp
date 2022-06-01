package com.fip.flexisaf.repositories;

import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.models.Review;
import com.fip.flexisaf.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ReviewRepositoryTest {
    @Autowired
    ReviewRepository reviewRepository;
    
    @Autowired
    FilmRepository filmRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @BeforeEach
    void beforeEach(){
        Film film1 = new Film().setName("Avatar");
        Film film2 = new Film().setName("Con Air");
        filmRepository.saveAll(List.of(film1, film2));
        
        User alice = new User().setEmail("alice@film.com");
        User bob = new User().setEmail("bob@film.com");
        userRepository.saveAll(List.of(alice, bob));
    }
    
    @AfterEach
    void tearDown(){
        //reviewRepository.deleteAll();
        filmRepository.deleteAll();
        userRepository.deleteAll();
    }
    
    @Test
    void findReviewByFilmNameTest(){
        reviewRepository.saveAll(List.of(
                new Review().setFilm(filmRepository.findFilmByName("Avatar").get()),
                new Review().setFilm(filmRepository.findFilmByName("Avatar").get()))
        );
        List<Review>avatarReview = reviewRepository.findReviewByFilmName("Avatar");
        assertThat(avatarReview.size()).isEqualTo(2);
    }
    
    @Test
    void findReviewByUserTest(){
        reviewRepository.saveAll(List.of(
                new Review().setUser(userRepository.findUserByEmail("bob@film.com").get()),
                new Review().setUser(userRepository.findUserByEmail("bob@film.com").get())
                )
        );
        List<Review>avatarReview = reviewRepository.findReviewByUser("bob@film.com");
        assertThat(avatarReview.size()).isEqualTo(2);
    }
    
    @Test
    void findReviewByUserAndFilmTest(){
        reviewRepository.saveAll(List.of(
                new Review().setFilm(filmRepository.findFilmByName("Avatar").get())
                        .setUser(userRepository.findUserByEmail("bob@film.com").get()),
                new Review().setFilm(filmRepository.findFilmByName("Con Air").get())
                        .setUser(userRepository.findUserByEmail("bob@film.com").get()))
        );
        List<Review>avatarReview = reviewRepository.findReviewByUserAndFilm("bob@film.com", "Avatar");
        assertThat(avatarReview.size()).isEqualTo(1);
    }
    
    @Test
    public void createAndDeleteReviewsTest(){
        reviewRepository.deleteAll();
        reviewRepository.saveAll(List.of(
                new Review().setFilm(filmRepository.findFilmByName("Con Air").get()),
                new Review().setFilm(filmRepository.findFilmByName("Con Air").get())
        ));
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList.size()).isEqualTo(2);
        
        reviewRepository.deleteAll();
        List<Review> reviewList2 = reviewRepository.findAll();
        assertThat(reviewList2.size()).isEqualTo(0);
    }
}
