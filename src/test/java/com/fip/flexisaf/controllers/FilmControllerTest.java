package com.fip.flexisaf.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fip.flexisaf.FilmappApplication;
import com.fip.flexisaf.repositories.FilmRepository;
import com.fip.flexisaf.controllers.requests.film.FilmRequest;
import com.fip.flexisaf.models.Film;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {FilmappApplication.class})
public class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    FilmRepository filmRepository;
    
    private final String URI = "/api/v1/film";
    
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
        //filmRepository.deleteAll();
    }
    
    @AfterEach
    void tearDown(){
        filmRepository.deleteAll();
    }
    
    @Test
    public void addFilmTest() throws Exception {
        FilmRequest newFilmRequest = getFilm();
        
        MvcResult result = mockMvc.perform(
                                          post(URI)
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  //.content(mapper.writeValueAsString(newFilmRequest))
                                                  .content(mapToJson(newFilmRequest))
                                                  .accept(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isCreated())
                                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(jsonPath("$.id").isNumber())
                                  .andExpect(jsonPath("$.name").value(newFilmRequest.getName()))
                                  .andReturn();
    
//        mockMvc.perform(
//                       post(URI)
//                               .contentType(MediaType.APPLICATION_JSON)
//                               //.content(mapper.writeValueAsString(newFilmRequest))
//                               .content(mapToJson(newFilmRequest))
//                               .accept(MediaType.APPLICATION_JSON))
//               .andExpect(status().isConflict())
//               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//               .andExpect(jsonPath("$.error_message").value("Film " + newFilmRequest.getName() + " already exists."))
//               .andExpect(jsonPath("$.error_code").value("CONFLICT"));
    
        //Film film = mapper.readValue(result.getResponse().getContentAsString(), Film.class);
        Film film = mapFromJson(result.getResponse().getContentAsString(), Film.class);
        assertNotNull(film);
        assertEquals(film.getName(), newFilmRequest.getName());
    }
    
    @Test
    public void getAllTest() throws Exception {
        FilmRequest newFilmRequest = getFilm();
        mockMvc.perform(
                      post(URI)
                              .contentType(MediaType.APPLICATION_JSON)
                              //.content(mapper.writeValueAsString(newFilmRequest))
                              .content(mapToJson(newFilmRequest))
                              .accept(MediaType.APPLICATION_JSON))
              .andExpect(status().isCreated())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        MvcResult result = mockMvc.perform(get(URI+"/all"))
                                  .andExpect(status().isOk())
                                  .andReturn();
    
        String content = result.getResponse().getContentAsString();
        Film[] films = mapFromJson(content, Film[].class);
        assertEquals(3, films.length);
    }
    
    @Test
    public void getOneTest() throws Exception {
        FilmRequest newFilmRequest = getFilm();
        mockMvc.perform(get(URI+"/" + 10L))
               .andExpect(status().isNotFound());
               //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
               //.andExpect(jsonPath("$.error_message").value("Could not find film with ID: " + 10L));
               //.andExpect(jsonPath("$.error_code").value("NOT_FOUND"));
    
    
        MvcResult postResult = mockMvc.perform(
                       post(URI)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(mapToJson(newFilmRequest))
                               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name").value(newFilmRequest.getName()))
                .andReturn();
    
        String content = postResult.getResponse().getContentAsString();
        Film postedFilm = mapFromJson(content, Film.class);
        
        MvcResult result = mockMvc
                .perform(get(URI+"/"+postedFilm.getId()))
                //.andExpect(status().isFound())
                .andReturn();
    
        String response = result.getResponse().getContentAsString();
        Film film = mapFromJson(response, Film.class);
    
        assertEquals(film.getName(), newFilmRequest.getName());
        assertEquals(film.getReleaseDate(), newFilmRequest.getReleaseDate());
    }
    
    @Test
    public void deleteTest() throws Exception {
        FilmRequest newFilmRequest = getFilm();
        mockMvc.perform(delete(URI+"/"+9L))
               .andExpect(status().isNotFound());
               //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
               //.andExpect(jsonPath("$.error_message").value("Exam with number " + newExam.getExamNumber() + " not found."))
               //.andExpect(jsonPath("$.error_code").value("NOT_FOUND"));
    
    
        MvcResult postedFilm = mockMvc.perform(
                       post(URI)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(mapToJson(newFilmRequest))
                               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name").value(newFilmRequest.getName()))
               .andExpect(jsonPath("$.release_date").value(newFilmRequest.getReleaseDate().toString()))
                .andReturn();
        
        Film film = mapFromJson(postedFilm.getResponse().getContentAsString(), Film.class);
        mockMvc
                .perform(delete(URI+"/"+film.getId()))
                .andExpect(status().isOk());
    
        MvcResult result = mockMvc.perform(get(URI+"/all"))
                                  .andExpect(status().isOk())
                                  .andReturn();
    
        String content = result.getResponse().getContentAsString();
        Film[] exams = mapFromJson(content, Film[].class);
        assertEquals(2, exams.length);
    }
    
    @Test
    public void updateTest() throws Exception {
        FilmRequest newFilmRequest = getFilm();
        MvcResult result = mockMvc.perform(
                                  post(URI)
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(mapToJson(newFilmRequest))
                                          .accept(MediaType.APPLICATION_JSON))
                          .andExpect(status().isCreated())
                          //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                          .andExpect(jsonPath("$.name").value(newFilmRequest.getName()))
                          .andExpect(jsonPath("$.release_date").value(newFilmRequest.getReleaseDate().toString()))
                          .andReturn();
    
        Film film = mapFromJson(result.getResponse().getContentAsString(), Film.class);
    
        film.setRating(8.0);
        film.setDescription("new Description");
        
        MvcResult updatedResult = mockMvc.perform(
                                                 put(URI)
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .content(mapToJson(film))
                                                         .accept(MediaType.APPLICATION_JSON))
                                         .andExpect(status().isOk())
                                         //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                         .andReturn();
    
        String updatedContent = updatedResult.getResponse().getContentAsString();
        Film updatedFilm = mapFromJson(updatedContent, Film.class);
    
        assertEquals(updatedFilm.getId(), film.getId());
        assertNotEquals(updatedFilm.getRating(), newFilmRequest.getRating());
        assertNotEquals(updatedFilm.getDescription(), newFilmRequest.getDescription());
    }
    
    FilmRequest getFilm(){
        return new FilmRequest()
                .setDescription("Film description")
                .setRating(9.0)
                .setGenre("film genre")
                .setName("Film name")
                .setReleaseDate(LocalDate.now()); //.parse("2022-05-27"));
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
