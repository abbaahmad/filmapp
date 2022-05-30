package com.fip.flexisaf.controllers.requests.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateReviewRequest extends ReviewRequest {
    @NotEmpty(message = "Id cannot be empty.")
    private Long id;
}
