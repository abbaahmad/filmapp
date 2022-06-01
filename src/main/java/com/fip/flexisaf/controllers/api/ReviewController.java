package com.fip.flexisaf.controllers.api;

import com.fip.flexisaf.controllers.requests.review.ReviewRequest;
import com.fip.flexisaf.controllers.requests.review.UpdateReviewRequest;
import com.fip.flexisaf.models.Review;
import com.fip.flexisaf.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path="/api/v1/film/review")
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    
    @GetMapping("/all")
    public List<Review> getAll(@RequestParam(required = false, defaultValue = "") String filmName,
                               @RequestParam(required = false, defaultValue = "") String username,
                               @AuthenticationPrincipal UserDetails userDetails){
        return reviewService.getAll(filmName, username, userDetails);
    }
    
    @GetMapping("/{reviewId}")
    public Review getOne(@PathVariable Long reviewId, @AuthenticationPrincipal UserDetails userDetails){
        return reviewService.getOne(reviewId, userDetails);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review add(@Valid @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal UserDetails userDetails){
        return reviewService.add(reviewRequest, userDetails);
    }
    
    @DeleteMapping("/{reviewId}")
    public void delete(@PathVariable Long reviewId, @AuthenticationPrincipal UserDetails userDetails){
        reviewService.delete(reviewId, userDetails);
    }
    
    @PutMapping
    public Review update(@Valid @RequestBody UpdateReviewRequest updateReviewRequest, @AuthenticationPrincipal UserDetails userDetails){
        return reviewService.update(updateReviewRequest, userDetails);
    }
}
