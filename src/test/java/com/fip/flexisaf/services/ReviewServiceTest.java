package com.fip.flexisaf.services;

import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.models.Role;
import com.fip.flexisaf.models.User;
import com.fip.flexisaf.repositories.FilmRepository;
import com.fip.flexisaf.repositories.ReviewRepository;
import com.fip.flexisaf.controllers.requests.review.ReviewRequest;
import com.fip.flexisaf.controllers.requests.review.UpdateReviewRequest;
import com.fip.flexisaf.exceptions.ResourceNotFoundException;
import com.fip.flexisaf.models.Review;
import com.fip.flexisaf.models.mappers.ReviewMapper;
import com.fip.flexisaf.repositories.UserRepository;
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
    
    @Mock
    UserRepository userRepository;
    
    @Autowired
    @InjectMocks
    ReviewServiceImpl reviewService;
    
    @Test
    public void addReviewTest(){
        ReviewRequest reviewRequest = getReview();
        Review review = ReviewMapper.toReview(reviewRequest);
        review.setCreatedOn(LocalDate.now());
        
        User bob = new User()
                .setEmail("bobreed@film.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.REGISTERED);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
    
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(filmRepository.findFilmByName(any(String.class))).thenReturn(Optional.of(new Film()));
        
        Review newReview = reviewService.add(reviewRequest, bob);
        assertThat(newReview).isNotNull();
        assertThat(newReview.getCreatedOn()).isEqualTo(LocalDate.now());
    }
    
    
    @Test
    public void getOneTest(){
        Long id = 1L;
        User bob = new User()
                .setEmail("bobreed@film.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.REGISTERED);
    
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
    
        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
            reviewService.delete(id, bob);
        });
        
        assertEquals(e.getMessage(), "Could not find review with ID: " + id);
        
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(new Review()));
        
        Review review = reviewService.getOne(id, bob);
        
        assertThat(review).isNotNull();
    }
    
    @Test
    public void getAllByUserTest(){
        User bob = new User()
                .setEmail("bobreed@film.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.REGISTERED);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
    
        when(reviewRepository.findReviewByUser(any(String.class))).thenReturn(List.of(new Review(), new Review()));
        
        List<Review> reviewList = reviewService.getAll("","",bob);
        assertThat(reviewList.size()).isEqualTo(2);
    
        when(reviewRepository.findReviewByFilmName(any(String.class)))
                .thenReturn(List.of(new Review(), new Review(), new Review()));
    
        List<Review> reviewList1 = reviewService.getAll("filmName","",bob);
        assertThat(reviewList1.size()).isEqualTo(3);
    }
    
    @Test
    public void getAllByAdminTest(){
        User bob = new User()
                .setEmail("bobreed@film.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.ADMINISTRATOR);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
    
        when(reviewRepository.findAll()).thenReturn(List.of(new Review(), new Review(), new Review()));
        
        List<Review> reviewList1 = reviewService.getAll("","",bob);
        assertThat(reviewList1.size()).isEqualTo(3);
    
        when(reviewRepository.findReviewByUser(any(String.class))).thenReturn(List.of(new Review(), new Review()));
        
        List<Review> reviewList2 = reviewService.getAll("","bob",bob);
        assertThat(reviewList2.size()).isEqualTo(2);
        
        when(reviewRepository.findReviewByFilmName(any(String.class)))
                .thenReturn(List.of(new Review(), new Review(), new Review()));
    
        List<Review> reviewList3 = reviewService.getAll("filmName","",bob);
        assertThat(reviewList3.size()).isEqualTo(3);
        
        when(reviewRepository.findReviewByUserAndFilm(any(String.class),any(String.class)))
                .thenReturn(List.of(new Review()));
    
        List<Review> reviewList4 = reviewService.getAll("filmName","bob",bob);
        assertThat(reviewList4.size()).isEqualTo(1);
    }
    
    @Test
    public void deleteTest(){
        Long id = 1L;
    
        User bob = new User()
                .setEmail("bobreed@film.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.REGISTERED);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
    
        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
            reviewService.delete(id, bob);
        });
        
        assertEquals(e.getMessage(), "Could not find review with ID: " + id);
        
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(new Review()));
    
        reviewService.delete(id, bob);
        
        verify(reviewRepository, times(1)).delete(any(Review.class));
    }
    
    @Test
    public void updateTest(){
        Long id = 2L;
    
        User bob = new User()
                .setEmail("bobreed@film.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.REGISTERED);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
    
    
        UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest()
                .setId(id);
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(new Review()));
        when(reviewRepository.save(any(Review.class))).thenReturn(ReviewMapper.toReview(updateReviewRequest));
    
        Review review = reviewService.update(updateReviewRequest, bob);
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
