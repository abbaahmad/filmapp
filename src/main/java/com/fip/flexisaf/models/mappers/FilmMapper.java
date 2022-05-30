package com.fip.flexisaf.models.mappers;

import com.fip.flexisaf.controllers.requests.film.FilmRequest;
import com.fip.flexisaf.controllers.requests.film.UpdateFilmRequest;
import com.fip.flexisaf.models.Film;

public class FilmMapper {
    public static Film toFilm(FilmRequest filmRequest){
        return new Film()
                .setDescription(filmRequest.getDescription())
                .setName(filmRequest.getName())
                .setGenre(filmRequest.getGenre())
                .setReleaseDate(filmRequest.getReleaseDate())
                .setRating(filmRequest.getRating());
    }
    public static Film toFilm(UpdateFilmRequest updateFilmRequest){
        return new Film()
            .setId(updateFilmRequest.getId())
            .setDescription(updateFilmRequest.getDescription())
            .setName(updateFilmRequest.getName())
            .setGenre(updateFilmRequest.getGenre())
            .setReleaseDate(updateFilmRequest.getReleaseDate())
            .setRating(updateFilmRequest.getRating());
    }
}
