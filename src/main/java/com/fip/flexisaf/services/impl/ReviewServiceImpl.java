package com.fip.flexisaf.services.impl;

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
import com.fip.flexisaf.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    ReviewRepository reviewRepository;
    
    @Autowired
    FilmRepository filmRepository;
    
    @Autowired
    UserRepository userRepository;
    
    public List<Review> getAll(String filmName, String username, UserDetails userDetails){
        User currentUser = userRepository.findUserByEmail(userDetails.getUsername())
                                         .orElseThrow(() -> new ResourceNotFoundException("No such User "+userDetails.getUsername()));
        
        if(currentUser.getRole().equals(Role.REGISTERED)){
            if(!filmName.equals(""))
                return reviewRepository.findReviewByFilmName(filmName);
            
            return reviewRepository.findReviewByUser(currentUser.getUsername());
        }
    
        if (currentUser.getRole().equals(Role.ADMINISTRATOR)){
            if(!username.equals("") && !filmName.equals(""))
                return reviewRepository.findReviewByUserAndFilm(username, filmName);
                
            if(!username.equals(""))
                return reviewRepository.findReviewByUser(username);
            
            if(!filmName.equals(""))
                return reviewRepository.findReviewByFilmName(filmName);
        }
        
        return reviewRepository.findAll();
    }
    public Review getOne(Long id, UserDetails userDetails){
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                                  .orElseThrow(() -> {
                                      throw new ResourceNotFoundException("No such User "+userDetails.getUsername());
                                  });
        if(user.getRole().equals(Role.REGISTERED) || user.getRole().equals(Role.ADMINISTRATOR))
            return reviewRepository.findById(id)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Could not find review with ID: " +id);
                });
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credentials unauthorised for such action");
    }
    
    public Review add(ReviewRequest reviewRequest, UserDetails userDetails) {
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                                  .orElseThrow(() -> {
                                      throw new ResourceNotFoundException("No such User "+userDetails.getUsername());
                                  });
        if(user.getRole().equals(Role.REGISTERED) || user.getRole().equals(Role.ADMINISTRATOR)) {
            if (filmRepository.findFilmByName(reviewRequest.getFilm().getName()).isEmpty())
                throw new ResourceNotFoundException("Cannot find film " + reviewRequest.getFilm().getName()
                                                            + " to be reviewed");
            Review r = ReviewMapper.toReview(reviewRequest);
            r.setCreatedOn(LocalDate.now());
            r.setLastModified(LocalDate.now());
            return reviewRepository.save(r);
        }
        throw new ResourceNotFoundException("No such User "+userDetails.getUsername());
    }
    
    public void delete(Long reviewId, UserDetails userDetails) {
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("No such User "+userDetails.getUsername());
                });
        Review r = reviewRepository.findById(reviewId)
                                   .orElseThrow(() -> {
                                       throw new ResourceNotFoundException("Could not find review with ID: " + reviewId);
                                   });
        if(user.getRole().equals(Role.ADMINISTRATOR) || r.getUser().getUsername().equals(user.getUsername())) {
            reviewRepository.delete(r);
        } else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credentials unauthorised for such action");
        }
    }
    
    @Transactional
    public Review update(UpdateReviewRequest updateReviewRequest, UserDetails userDetails) {
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                                  .orElseThrow(() -> {
                                      throw new ResourceNotFoundException("No such User "+userDetails.getUsername());
                                  });
        if(user.getRole().equals(Role.REGISTERED) || user.getRole().equals(Role.ADMINISTRATOR)) {
            if (reviewRepository.findById(updateReviewRequest.getId()).isEmpty())
                throw new ResourceNotFoundException("Could not find review with ID: " + updateReviewRequest.getId());
    
            Review updatedReview = ReviewMapper.toReview(updateReviewRequest);
            updatedReview.setCreatedOn(LocalDate.now());
            updatedReview.setLastModified(LocalDate.now());
            return reviewRepository.save(updatedReview);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credentials unauthorised for such action");
    }
}
