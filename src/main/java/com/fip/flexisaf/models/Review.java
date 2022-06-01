package com.fip.flexisaf.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Table(name="reviews")
@Entity
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Review {
    @Id
    @SequenceGenerator(
            name="review_sequence",
            sequenceName = "review_sequence",
            allocationSize = 1,
            schema = "public"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "review_sequence"
    )
    private Long id;
    
    @ManyToOne(targetEntity=Film.class, fetch=FetchType.LAZY)
    @JoinColumn(name="film_id")
    @JsonIncludeProperties({"id", "name"})
    private Film film;
    
    @CreatedDate
    private LocalDate createdOn;
    
    @LastModifiedDate
    private LocalDate lastModified;
    
    @ManyToOne(targetEntity=User.class, fetch=FetchType.LAZY)
    @JoinColumn(name="users_id")
    @JsonIncludeProperties({"id","username"})
    private User user;
    
    private Double userRating;
    
    private String review;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Review review = (Review) o;
        return id != null && Objects.equals(id, review.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
