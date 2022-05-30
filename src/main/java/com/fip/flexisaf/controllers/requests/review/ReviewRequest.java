package com.fip.flexisaf.controllers.requests.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fip.flexisaf.models.Film;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRequest {
    //@NotNull(message = "Review created on")
    //@JsonProperty("created_on")
    //private LocalDate createdOn;
    
    //@NotNull(message = "Review last modified on")
    //@JsonProperty("last_modified")
    //private LocalDate lastModified;
    //TODO: Remove lastModified
    
    @Min(value = 1, message = "Rating cannot be less than 1")
    @Max(value = 10, message = "Rating cannot be more than 10")
    @JsonProperty("user_rating")
    private double userRating;
    
    @NotNull(message="Cannot review a non-existent film")
    private Film film;
    
    @NotBlank(message="Review text cannot be empty")
    private String review;
}
