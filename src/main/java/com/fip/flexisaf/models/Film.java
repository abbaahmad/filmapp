package com.fip.flexisaf.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Table(name="films")
@Entity
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Film {
    @Id
    @SequenceGenerator(
            name="film_sequence",
            sequenceName = "film_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "film_sequence"
    )
    private Long id;
    private String name;
    private String genre;
    private String description;
    private Double rating;
    private LocalDate releaseDate;
    //@OneToMany(targetEntity=Review.class, cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
    //@JoinColumn(name="review_id")
    //TODO: Check to make sure cascade option is the right choice
    //private List<Review> reviewList;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Film film = (Film) o;
        return id != null && Objects.equals(id, film.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
