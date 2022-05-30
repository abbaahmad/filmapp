package com.fip.flexisaf.services;

import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.repositories.FilmRepository;
import com.fip.flexisaf.repositories.ReviewRepository;
import com.fip.flexisaf.controllers.requests.review.ReviewRequest;
import com.fip.flexisaf.controllers.requests.review.UpdateReviewRequest;
import com.fip.flexisaf.exceptions.ResourceNotFoundException;
import com.fip.flexisaf.models.Review;
import com.fip.flexisaf.models.mappers.ReviewMapper;
import com.fip.flexisaf.services.impl.ReviewServiceImpl;
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
public class ReviewServiceTest {
    @Mock
    ReviewRepository reviewRepository;
    
    @Mock
    FilmRepository filmRepository;
    
    @Autowired
    @InjectMocks
    ReviewServiceImpl reviewService;
    
    @Test
    public void addReviewTest(){
        ReviewRequest reviewRequest = getReview();
        Review review = ReviewMapper.toReview(reviewRequest);
        review.setCreatedOn(LocalDate.now());
        
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(filmRepository.findFilmByName(any(String.class))).thenReturn(Optional.of(new Film()));
        
        Review newReview = reviewService.add(reviewRequest);
        assertThat(newReview).isNotNull();
        assertThat(newReview.getCreatedOn()).isEqualTo(LocalDate.now());
    }
    
    
    @Test
    public void getOneTest(){
        Long id = 1L;
        
        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
            reviewService.delete(id);
        });
        
        assertEquals(e.getMessage(), "Could not find review with ID: " + id);
        
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(new Review()));
        
        Review review = reviewService.getOne(id);
        
        assertThat(review).isNotNull();
    }
    
    @Test
    public void getAllTest(){
        when(reviewRepository.findAll()).thenReturn(List.of(new Review(), new Review()));
        
        List<Review> reviewList = reviewService.getAll();
        assertThat(reviewList.size()).isEqualTo(2);
    }
    
    @Test
    public void deleteTest(){
        Long id = 1L;
        
        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
            reviewService.delete(id);
        });
        
        assertEquals(e.getMessage(), "Could not find review with ID: " + id);
        
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(new Review()));
    
        reviewService.delete(id);
        
        verify(reviewRepository, times(1)).delete(any(Review.class));
    }
    
    @Test
    public void updateTest(){
        Long id = 2L;
        
        
        UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest()
                .setId(id);
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(new Review()));
        when(reviewRepository.save(any(Review.class))).thenReturn(ReviewMapper.toReview(updateReviewRequest));
    
        Review review = reviewService.update(updateReviewRequest);
        assertEquals(review.getId(), id);
    }
    
    ReviewRequest getReview(){
        return new ReviewRequest()
                .setReview("Review text")
                .setFilm(new Film().setName("Film name"))
                .setUserRating(9.0);
                //.setCreatedOn(LocalDate.now())
                //.setLastModified(LocalDate.now());
    }
}
