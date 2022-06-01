package com.fip.flexisaf.models.mappers;

import com.fip.flexisaf.controllers.requests.review.ReviewRequest;
import com.fip.flexisaf.controllers.requests.review.UpdateReviewRequest;
import com.fip.flexisaf.models.Review;

public class ReviewMapper {
    public static Review toReview(ReviewRequest reviewRequest){
        return new Review()
                .setReview(reviewRequest.getReview())
                .setFilm(reviewRequest.getFilm())
                //.setCreatedOn(reviewRequest.getCreatedOn())
                //.setLastModified(reviewRequest.getLastModified())
                .setUser(reviewRequest.getUser())
                .setUserRating(reviewRequest.getUserRating());
    }
    
    public static Review toReview(UpdateReviewRequest updateReviewRequest){
        return new Review()
                .setId(updateReviewRequest.getId())
                .setReview(updateReviewRequest.getReview())
                .setFilm(updateReviewRequest.getFilm())
                .setUser(updateReviewRequest.getUser())
                //.setCreatedOn(updateReviewRequest.getCreatedOn())
                //.setLastModified(LocalDate.now())
                .setUserRating(updateReviewRequest.getUserRating());
    }
}
