package com.fip.flexisaf.controllers.requests.film;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class FilmRequest {
    
    @NotBlank(message = "Film name cannot be blank")
    private String name;
    
    @NotBlank(message = "Film genre cannot be blank")
    private String genre;
    
    @NotBlank(message = "Film description cannot be blank")
    private String description;
    
    @Min(value = 1, message = "Rating cannot be less than 1")
    @Max(value = 10, message = "Rating cannot be more than 10")
    private double rating;
    
    @JsonProperty("release_date")
    @NotNull(message = "Release date of film cannot be blank")
    private LocalDate releaseDate;
}
