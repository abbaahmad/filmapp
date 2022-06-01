package com.fip.flexisaf;

import com.fip.flexisaf.controllers.requests.review.ReviewRequest;
import com.fip.flexisaf.models.Review;
import com.fip.flexisaf.models.Role;
import com.fip.flexisaf.models.User;
import com.fip.flexisaf.repositories.FilmRepository;
import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.repositories.ReviewRepository;
import com.fip.flexisaf.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataLoader implements ApplicationRunner {
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    FilmRepository filmRepository;
    
    @Autowired
    ReviewRepository reviewRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        User admin = new User()
                .setName("Admin")
                .setEmail("admin@film.com")
                .setPassword(encoder.encode("administrator"))
                .setEnabled(true)
                .setRole(Role.ADMINISTRATOR);
    
        if(userRepository.findUserByEmail(admin.getUsername()).isEmpty()){
            userRepository.save(admin);
        }
        /*Film avatar = new Film()
                .setName("Avatar")
                .setGenre("Action")
                .setDescription("A paraplegic marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home")
                .setRating(7.8)
                .setReleaseDate(LocalDate.parse("2009-12-10"));
        Film pirates = new Film()
                .setName("Pirates of the Caribbean")
                .setGenre("Action")
                .setDescription("Captain Barbossa, Will Turner and Elizabeth Swann must sail off the edge of the map, navigate treachery and betrayal, find Jack Sparrow, and make their final alliances for one last decisive battle")
                .setRating(7.1)
                .setReleaseDate(LocalDate.parse("2003-06-28"));
        filmRepository.saveAll(List.of(avatar, pirates));
        reviewRepository.saveAll(List.of(
                new Review()
                    .setReview("1st Film review")
                    .setUserRating(9.0)
                    .setCreatedOn(LocalDate.now())
                    .setLastModified(LocalDate.now())
                    .setFilm(avatar),
                new Review()
                    .setReview("2nd Film review")
                    .setUserRating(9.8)
                    .setCreatedOn(LocalDate.now())
                    .setLastModified(LocalDate.now())
                    .setFilm(avatar)
        ));*/
    }
}
