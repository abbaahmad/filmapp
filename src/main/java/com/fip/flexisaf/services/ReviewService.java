package com.fip.flexisaf.services;

import com.fip.flexisaf.controllers.requests.review.ReviewRequest;
import com.fip.flexisaf.controllers.requests.review.UpdateReviewRequest;
import com.fip.flexisaf.models.Review;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ReviewService {
    Review add(ReviewRequest reviewRequest, UserDetails userDetails);
    Review getOne(Long reviewId, UserDetails userDetails);
    List<Review> getAll(String filmName, String username, UserDetails userDetails);
    void delete(Long reviewId, UserDetails userDetails);
    Review update(UpdateReviewRequest updateReviewRequest, UserDetails userDetails);
}
