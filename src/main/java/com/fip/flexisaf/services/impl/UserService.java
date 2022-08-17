package com.fip.flexisaf.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fip.flexisaf.controllers.requests.user.NewUserRequest;
import com.fip.flexisaf.controllers.requests.user.UserLoginRequest;
import com.fip.flexisaf.models.User;
import com.fip.flexisaf.models.dto.UserDto;
import com.fip.flexisaf.models.mappers.UserMapper;
import com.fip.flexisaf.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder encoder;
    
    public String saveUser(NewUserRequest newUserRequest) {
        if(userRepository.findUserByEmail(newUserRequest.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist!");
        }
    
        User user = UserMapper.toUser(newUserRequest);
        user.setPassword(encoder.encode(newUserRequest.getPassword()));
        User savedUser = userRepository.save(user);
    
//        return UserMapper.toUserDto(savedUser);
        return getTokens(savedUser);
    }
    
    public UserDto loadUser(UserLoginRequest userLoginRequest){
        User user = userRepository.findUserByEmail(userLoginRequest.getEmail().toLowerCase())
                                  .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such User"));
    
        if(!encoder.matches(userLoginRequest.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect credentials");
        }
    
        return UserMapper.toUserDto(user);
    }
    
    public UserDto getOne(String username) {
        User user = userRepository.findUserByEmail(username.toLowerCase())
                             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such User: "
                             + username));
        
        return UserMapper.toUserDto(user);
    }
    
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
    
        return users.stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username)
                             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such User"));
    }
    
    private String getTokens(User user){
        //org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) user;
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); //ECSDA algorithm
        String accessToken = JWT.create()
                                .withSubject(user.getUsername())
                                .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                                .withClaim("roles", new ArrayList<>(user.getAuthorities()))
                                .sign(algorithm);
        String refreshToken = JWT.create()
                                 .withSubject(user.getUsername())
                                 .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
                                 .withClaim("roles", new ArrayList<>(user.getAuthorities()))
                                 .sign(algorithm);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens.toString();
    }
}
