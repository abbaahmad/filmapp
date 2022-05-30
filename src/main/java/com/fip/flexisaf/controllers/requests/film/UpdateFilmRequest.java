package com.fip.flexisaf.controllers.requests.film;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateFilmRequest extends FilmRequest {
    @NotEmpty(message = "Id cannot be empty.")
    private Long id;
}
