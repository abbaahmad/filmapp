package com.fip.flexisaf.services.impl;

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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder encoder;
    
    public UserDto saveUser(NewUserRequest newUserRequest) {
        if(userRepository.findUserByEmail(newUserRequest.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist!");
        }
    
        User user = UserMapper.toUser(newUserRequest);
        user.setPassword(encoder.encode(newUserRequest.getPassword()));
        User savedUser = userRepository.save(user);
    
        return UserMapper.toUserDto(savedUser);
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
}
