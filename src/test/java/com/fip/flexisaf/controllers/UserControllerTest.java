package com.fip.flexisaf.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fip.flexisaf.FilmappApplication;
import com.fip.flexisaf.controllers.requests.user.NewUserRequest;
import com.fip.flexisaf.controllers.requests.user.UserLoginRequest;
import com.fip.flexisaf.models.Role;
import com.fip.flexisaf.models.User;
import com.fip.flexisaf.models.dto.UserDto;
import com.fip.flexisaf.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {FilmappApplication.class})
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;
    
    private final String URI = "/api/v1/auth";
    
    @BeforeEach
    void tearDown() {
        userRepository.deleteAll();
    }
    
    @Test
    public void createAndRegisterUserTest() throws Exception {
        User alice = new User().setName("Alice Alex")
                               .setEmail("aalex@film.com")
                               .setPassword("aliceAlex123")
                               .setRole(Role.REGISTERED);
        NewUserRequest aliceRequest = new NewUserRequest()
                .setName("Alice Alex")
                .setEmail("aalex@film.com")
                .setPassword("aliceAlex123")
                .setRole(Role.REGISTERED);
        
        MvcResult result = mockMvc.perform(
                                          post(URI+"/register")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(mapToJson(aliceRequest))
                                                  .accept(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isCreated())
                                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                  //.andExpect(jsonPath("$.id").isString())
                                  .andExpect(jsonPath("$.name").value(alice.getName()))
                                  .andReturn();
        
        mockMvc.perform(
                       post(URI+"/register")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(mapToJson(alice))
                               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isConflict());
               //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
               //.andExpect(jsonPath("$.error_message").value("409 CONFLICT \"User exist!\""))
               //.andExpect(jsonPath("$.error_code").value("409 CONFLICT"));
        
        UserDto aliceResult = mapFromJson(result.getResponse().getContentAsString(), UserDto.class);
        assertNotNull(aliceResult);
        assertEquals(aliceResult.getName(), alice.getName());
    }
    
    @Test
    public void getOneTest() throws Exception {
        User alice = new User().setName("Alice Alex")
                               .setEmail("aalex@film.com")
                               .setPassword("aliceAlex123")
                               .setRole(Role.REGISTERED);
        
        mockMvc.perform(
                       post(URI+"/register")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(mapToJson(alice))
                               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               //.andExpect(jsonPath("$.id").isString())
               .andExpect(jsonPath("$.name").value(alice.getName()))
               .andReturn();
        
        UserLoginRequest aliceLoginRequest = new UserLoginRequest()
                .setEmail("aalex@film.com")
                .setPassword("aliceAlex123");
        
        MvcResult result = mockMvc.perform(
                                          get(URI+"/"+aliceLoginRequest.getEmail())
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(mapToJson(aliceLoginRequest))
                                                  .accept(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk())
                                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                  //.andExpect(jsonPath("$.id").isString())
                                  .andExpect(jsonPath("$.email").value(aliceLoginRequest.getEmail()))
                                  .andReturn();
        
        UserDto aliceResult = mapFromJson(result.getResponse().getContentAsString(), UserDto.class);
        assertNotNull(aliceResult);
        assertEquals(aliceResult.getEmail(), aliceLoginRequest.getEmail());
    }
    
    @Test
    @WithMockUser(username = "admin@film.com", password = "administrator", authorities = {"ADMINISTRATOR"})
    public void getAllUsersTest() throws Exception {
        NewUserRequest alice = new NewUserRequest().setName("Alice Alex")
                               .setEmail("aalex@film.com")
                               .setPassword("aliceAlex123")
                               .setPassword("aliceAlex123")
                               .setRole(Role.REGISTERED);
        NewUserRequest bob = new NewUserRequest().setName("Robert Reed")
                             .setEmail("bobreed@film.com")
                             .setPassword("bobbyreeder12")
                             .setRole(Role.REGISTERED);
        
        mockMvc.perform(
                       post(URI+"/register")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(mapToJson(alice))
                               .accept(MediaType.APPLICATION_JSON))
               .andReturn();
        mockMvc.perform(
                       post(URI+"/register")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(mapToJson(bob))
                               .accept(MediaType.APPLICATION_JSON))
               .andReturn();
        
        MvcResult result = mockMvc.perform(get(URI+"/all"))
                                  .andExpect(status().isOk())
                                  .andReturn();
        
        String content = result.getResponse().getContentAsString();
        UserDto[] users = mapFromJson(content, UserDto[].class);
        assertEquals(2, users.length);
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
