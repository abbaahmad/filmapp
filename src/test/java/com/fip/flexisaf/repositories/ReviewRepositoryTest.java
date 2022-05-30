package com.fip.flexisaf.repositories;

import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.models.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ReviewRepositoryTest {
    @Autowired
    ReviewRepository reviewRepository;
    
    @Autowired
    FilmRepository filmRepository;
    
    @BeforeEach
    void setUp(){
        Film film1 = new Film().setName("Avatar");
        Film film2 = new Film().setName("Con Air");
        filmRepository.saveAll(List.of(film1, film2));
    }
    @AfterEach
    void tearDown(){
        reviewRepository.deleteAll();
        filmRepository.deleteAll();
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
    public void createAndDeleteReviewsTest(){
        reviewRepository.saveAll(List.of(
                new Review(),
                new Review()
        ));
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList.size()).isEqualTo(2);
    
        reviewRepository.deleteById(1L);
        List<Review> reviewList2 = reviewRepository.findAll();
        assertThat(reviewList2.size()).isEqualTo(1);
    
        reviewRepository.deleteAll();
        List<Review> reviewList3 = reviewRepository.findAll();
        assertThat(reviewList3.size()).isEqualTo(0);
    }
}
