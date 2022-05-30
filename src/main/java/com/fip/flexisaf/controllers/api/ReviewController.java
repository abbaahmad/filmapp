package com.fip.flexisaf.controllers.api;

import com.fip.flexisaf.controllers.requests.review.ReviewRequest;
import com.fip.flexisaf.controllers.requests.review.UpdateReviewRequest;
import com.fip.flexisaf.models.Review;
import com.fip.flexisaf.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path="api/v1/film/review")
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    
    @GetMapping("/all")
    public List<Review> getAll(){
        return reviewService.getAll();
    }
    
    @GetMapping("/{reviewId}")
    public Review getOne(@PathVariable Long reviewId){
        return reviewService.getOne(reviewId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review add(@Valid @RequestBody ReviewRequest reviewRequest){
        return reviewService.add(reviewRequest);
    }
    
    @DeleteMapping("/{reviewId}")
    public void delete(@PathVariable Long reviewId){
        reviewService.delete(reviewId);
    }
    
    @PutMapping
    public Review update(@Valid @RequestBody UpdateReviewRequest updateReviewRequest){
        return reviewService.update(updateReviewRequest);
    }
}
