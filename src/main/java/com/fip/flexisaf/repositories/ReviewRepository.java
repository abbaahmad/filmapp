package com.fip.flexisaf.repositories;

import com.fip.flexisaf.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    @Query("SELECT r FROM Review r WHERE r.film.id = (SELECT id FROM Film f WHERE f.name = ?1)")
    List<Review> findReviewByFilmName(String filmName);
    
    @Query("SELECT r FROM Review r WHERE r.user.id = (SELECT id FROM User u WHERE u.email = ?1)")
    List<Review> findReviewByUser(String username);

    @Query("SELECT r FROM Review r WHERE r.user.id = (SELECT id FROM User u WHERE u.email = ?1) AND " +
            "r.film.id = (SELECT id FROM Film f WHERE f.name = ?2)")
    List<Review> findReviewByUserAndFilm(String username, String filmName);
}
