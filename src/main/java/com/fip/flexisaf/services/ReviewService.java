package com.fip.flexisaf.services;

import com.fip.flexisaf.controllers.requests.review.ReviewRequest;
import com.fip.flexisaf.controllers.requests.review.UpdateReviewRequest;
import com.fip.flexisaf.models.Review;

import java.util.List;

public interface ReviewService {
    Review add(ReviewRequest reviewRequest);
    Review getOne(Long reviewId);
    List<Review> getAll();
    void delete(Long reviewId);
    Review update(UpdateReviewRequest updateReviewRequest);
}
