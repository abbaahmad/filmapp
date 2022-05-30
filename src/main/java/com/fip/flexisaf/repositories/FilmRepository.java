package com.fip.flexisaf.repositories;

import com.fip.flexisaf.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    @Query("SELECT f FROM Film f WHERE f.name = ?1")
    Optional<Film> findFilmByName(String name);
}
