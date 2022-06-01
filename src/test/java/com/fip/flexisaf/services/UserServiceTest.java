package com.fip.flexisaf.services;

import com.fip.flexisaf.controllers.requests.user.NewUserRequest;
import com.fip.flexisaf.controllers.requests.user.UserLoginRequest;
import com.fip.flexisaf.models.User;
import com.fip.flexisaf.models.dto.UserDto;
import com.fip.flexisaf.models.mappers.UserMapper;
import com.fip.flexisaf.repositories.UserRepository;
import com.fip.flexisaf.services.impl.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    
    @Autowired
    @InjectMocks
    UserService userService;
    
    @Spy
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    
    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }
    @Test
    public void createAndLoadUserByUsername(){
        UserLoginRequest bobLoginRequest = new UserLoginRequest()
                .setEmail("bobreed@cbt.com")
                .setPassword("bobbyreeder12");
    
        User bob = new User()
                .setEmail("bobreed@cbt.com")
                .setPassword(encoder.encode("bobbyreeder12"));
    
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
    
        UserDto loggedInUser = userService.loadUser(bobLoginRequest);
        assertThat(loggedInUser.getEmail()).isEqualTo(bob.getUsername());
    }
    
    @Test
    public void saveUserTest(){
        NewUserRequest bobRequest = new NewUserRequest()
                .setName("Robert Reed")
                .setEmail("bobreed@cbt.com")
                .setPassword("bobbyreeder12");
        
        User bobUser = UserMapper.toUser(bobRequest);
        when(userRepository.save(any(User.class))).thenReturn(bobUser);
        
        UserDto savedUser = userService.saveUser(bobRequest);
        assertThat(savedUser.getEmail()).isEqualTo("bobreed@cbt.com");
    }
    
    @Test
    public void getOneTest(){
        User bob = new User()
                .setEmail("bobreed@film.com")
                .setPassword("bobbyreeder12");
        
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
        
        UserDto loggedInUser = userService.getOne("bobreed@film.com");
        assertThat(loggedInUser.getEmail()).isEqualTo(bob.getUsername());
    }
    
    @Test
    public void getAllTest(){
        User alice = new User().setEmail("alice@film.com");
        User bob = new User().setEmail("bob@film.com");
        userRepository.saveAll(List.of(alice, bob));
        
        when(userRepository.findAll()).thenReturn(List.of(alice, bob));
        
        List<UserDto> loggedInUser = userService.getAll();
        assertThat(loggedInUser.size()).isEqualTo(2);
    }
}
