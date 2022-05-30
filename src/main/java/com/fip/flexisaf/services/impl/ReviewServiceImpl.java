package com.fip.flexisaf.services.impl;

import com.fip.flexisaf.repositories.FilmRepository;
import com.fip.flexisaf.repositories.ReviewRepository;
import com.fip.flexisaf.controllers.requests.review.ReviewRequest;
import com.fip.flexisaf.controllers.requests.review.UpdateReviewRequest;
import com.fip.flexisaf.exceptions.ResourceNotFoundException;
import com.fip.flexisaf.models.Review;
import com.fip.flexisaf.models.mappers.ReviewMapper;
import com.fip.flexisaf.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    ReviewRepository reviewRepository;
    
    @Autowired
    FilmRepository filmRepository;
    
    public List<Review> getAll(){
        return reviewRepository.findAll();
    }
    public Review getOne(Long id){
        return reviewRepository.findById(id)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Could not find review with ID: " +id);
                });
    }
    
    public Review add(ReviewRequest reviewRequest) {
        if(filmRepository.findFilmByName(reviewRequest.getFilm().getName()).isEmpty())
            throw new ResourceNotFoundException("Cannot find film " + reviewRequest.getFilm().getName()
                                                        +" to be reviewed");
        Review r = ReviewMapper.toReview(reviewRequest);
        r.setCreatedOn(LocalDate.now());
        r.setLastModified(LocalDate.now());
        return reviewRepository.save(r);
    }
    
    public void delete(Long reviewId) {
        Review r = reviewRepository.findById(reviewId)
                .orElseThrow( () -> {
                    throw new ResourceNotFoundException("Could not find review with ID: " + reviewId);
                });
        reviewRepository.delete(r);
    }
    
    @Transactional
    public Review update(UpdateReviewRequest updateReviewRequest) {
        if(reviewRepository.findById(updateReviewRequest.getId()).isEmpty())
            throw new ResourceNotFoundException("Could not find review with ID: "+updateReviewRequest.getId());
        
        Review updatedReview = ReviewMapper.toReview(updateReviewRequest);
        updatedReview.setCreatedOn(LocalDate.now());
        updatedReview.setLastModified(LocalDate.now());
        return reviewRepository.save(updatedReview);
    }
}
