package com.fip.flexisaf.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fip.flexisaf.FilmappApplication;
import com.fip.flexisaf.controllers.requests.review.ReviewRequest;
import com.fip.flexisaf.models.Film;
import com.fip.flexisaf.models.Review;
import com.fip.flexisaf.models.Role;
import com.fip.flexisaf.models.User;
import com.fip.flexisaf.repositories.FilmRepository;
import com.fip.flexisaf.repositories.ReviewRepository;
import com.fip.flexisaf.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {FilmappApplication.class})
public class ReviewControllerTest {
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ReviewRepository reviewRepository;
    
    @Autowired
    FilmRepository filmRepository;
    
    @Autowired
    UserRepository userRepository;
    
    private final String URI = "/api/v1/film/review";
    
    @BeforeEach
    void setUp(){
        Film pirates = new Film()
                .setName("Pirates of the Caribbean")
                .setGenre("Action")
                .setDescription("Captain Barbossa, Will Turner and Elizabeth Swann must sail off the edge of the map, navigate treachery and betrayal, find Jack Sparrow, and make their final alliances for one last decisive battle")
                .setRating(7.1)
                .setReleaseDate(LocalDate.parse("2003-06-28"));
        Film avatar = new Film()
                .setName("Avatar")
                .setGenre("Action")
                .setDescription("A paraplegic marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home")
                .setRating(7.8)
                .setReleaseDate(LocalDate.parse("2009-12-10"));
    
        filmRepository.saveAll(List.of(pirates, avatar));
        
        Review piratesReview = new Review()
                .setReview("Pirates review")
                .setCreatedOn(LocalDate.now())
                .setLastModified(LocalDate.now())
                .setUserRating(9.1)
                .setFilm(pirates);
        Review avatarReview = new Review()
                .setReview("Avatar review")
                .setCreatedOn(LocalDate.now())
                .setLastModified(LocalDate.now())
                .setUserRating(9.8)
                .setFilm(avatar);
    
        reviewRepository.saveAll(List.of(piratesReview, avatarReview));
    
        userRepository.save(new User()
                .setEmail("bobreed@film.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.REGISTERED));
    }
    
    @AfterEach
    void tearDown(){
        reviewRepository.deleteAll();
        filmRepository.deleteAll();
    }
    
    @Test
    @WithMockUser(username = "bobreed@film.com", password = "bobbyreeder12", authorities = {"ADMINISTRATOR"})
    public void addReviewTest() throws Exception {
        ReviewRequest newReviewRequest = getReview();
        
        MvcResult result = mockMvc.perform(
                                          post(URI)
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  //.content(mapper.writeValueAsString(newFilmRequest))
                                                  .content(mapToJson(newReviewRequest))
                                                  .accept(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isCreated())
                                  //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                  //.andExpect(jsonPath("$.id").isNumber())
                                  .andExpect(jsonPath("$.review").value(newReviewRequest.getReview()))
                                  .andReturn();
        
       
        //Film film = mapper.readValue(result.getResponse().getContentAsString(), Film.class);
        Review review = mapFromJson(result.getResponse().getContentAsString(), Review.class);
        assertNotNull(review);
        assertEquals(review.getFilm().getName(), newReviewRequest.getFilm().getName());
    }
    
    @Test
    @WithMockUser(username = "newuser@film.com", password = "newpassword", authorities = {"REGISTERED"})
    public void getAllTest() throws Exception{
        ReviewRequest newReviewRequest = getReview();
        mockMvc.perform(
                       post(URI)
                               .contentType(MediaType.APPLICATION_JSON)
                               //.content(mapper.writeValueAsString(newFilmRequest))
                               .content(mapToJson(newReviewRequest))
                               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    
        MvcResult result = mockMvc.perform(get(URI+"/all"))
                                  .andExpect(status().isOk())
                                  .andReturn();
    
        String content = result.getResponse().getContentAsString();
        Review[] reviews = mapFromJson(content, Review[].class);
        assertEquals(3, reviews.length);
    }
    
    @Test
    public void getOneTest() throws Exception {
        ReviewRequest newReviewRequest = getReview();
        mockMvc.perform(get(URI+"/" + 10L))
               .andExpect(status().isNotFound());
        //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        //.andExpect(jsonPath("$.error_message").value("Could not find film with ID: " + 10L));
        //.andExpect(jsonPath("$.error_code").value("NOT_FOUND"));
    
    
        MvcResult postResult = mockMvc.perform(
                                      post(URI)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapToJson(newReviewRequest))
                                              .accept(MediaType.APPLICATION_JSON))
                              .andExpect(status().isCreated())
                              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                              //.andExpect(jsonPath("$.created_on").value(newReviewRequest.getCreatedOn().toString()))
                              .andReturn();
    
        String content = postResult.getResponse().getContentAsString();
        Review postedReview = mapFromJson(content, Review.class);
    
        MvcResult result = mockMvc
                .perform(get(URI+"/"+postedReview.getId()))
                //.andExpect(status().isFound())
                .andReturn();
    
        String response = result.getResponse().getContentAsString();
        Review review = mapFromJson(response, Review.class);
    
        //assertEquals(review.getCreatedOn(), newReviewRequest.getCreatedOn());
        assertEquals(review.getUserRating(), newReviewRequest.getUserRating());
    }
    
    @Test
    public void deleteTest() throws Exception {
        ReviewRequest newReviewRequest = getReview();
        mockMvc.perform(delete(URI+"/"+9L))
               .andExpect(status().isNotFound());
        //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        //.andExpect(jsonPath("$.error_message").value("Exam with number " + newExam.getExamNumber() + " not found."))
        //.andExpect(jsonPath("$.error_code").value("NOT_FOUND"));
    
    
        MvcResult postedReview = mockMvc.perform(
                                      post(URI)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapToJson(newReviewRequest))
                                              .accept(MediaType.APPLICATION_JSON))
                              .andExpect(status().isCreated())
                              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                              //.andExpect(jsonPath("$.created_on").value(newReviewRequest.getCreatedOn().toString()))
                              .andReturn();
    
        Review review = mapFromJson(postedReview.getResponse().getContentAsString(), Review.class);
        mockMvc
                .perform(delete(URI+"/"+review.getId()))
                .andExpect(status().isOk());
    
        MvcResult result = mockMvc.perform(get(URI+"/all"))
                                  .andExpect(status().isOk())
                                  .andReturn();
    
        String content = result.getResponse().getContentAsString();
        Review[] reviews = mapFromJson(content, Review[].class);
        assertEquals(2, reviews.length);
    }
    
    @Test
    public void updateTest() throws Exception { //TODO: Passes but maybe problematic
        ReviewRequest newReviewRequest = getReview();
        MvcResult postedReview = mockMvc.perform(
                                                post(URI)
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(mapToJson(newReviewRequest))
                                                        .accept(MediaType.APPLICATION_JSON))
                                        .andExpect(status().isCreated())
                                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                        //.andExpect(jsonPath("$.created_on").value(newReviewRequest.getCreatedOn().toString()))
                                        .andReturn();
    
        Review r = mapFromJson(postedReview.getResponse().getContentAsString(), Review.class);
        
        r.setUserRating(8.0);
        r.setReview("new review text");
        
        MvcResult updatedResult = mockMvc.perform(
                                                 put(URI)
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .content(mapToJson(r))
                                                         .accept(MediaType.APPLICATION_JSON))
                                         .andExpect(status().isOk())
                                         //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                         .andReturn();
        
        Review updatedReview = mapFromJson(updatedResult.getResponse().getContentAsString(), Review.class);
    
        assertEquals(updatedReview.getId(), r.getId());
        assertNotEquals(updatedReview.getUserRating(), newReviewRequest.getUserRating());
        assertNotEquals(updatedReview.getReview(), newReviewRequest.getReview());
        //assertNotEquals(updatedReview.getLastModified(), newReviewRequest.getLastModified());
    }
    
    ReviewRequest getReview(){
        
        return new ReviewRequest()
                .setReview("Film review")
                .setUserRating(9.0)
                //.setCreatedOn(LocalDate.of(1999,10,10))
                //.setLastModified(LocalDate.of(1999,10,10))
                .setFilm(filmRepository.findFilmByName("Avatar").get());
                //.setFilm(new Film().setName("Avatar"));
    }
    
    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }
    
    private <T> T mapFromJson(String json, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.readValue(json, clazz);
    }
}
