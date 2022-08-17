package com.fip.flexisaf.controllers.api;

import com.fip.flexisaf.controllers.requests.user.NewUserRequest;
import com.fip.flexisaf.controllers.requests.user.UserLoginRequest;
import com.fip.flexisaf.models.dto.UserDto;
import com.fip.flexisaf.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody @Valid NewUserRequest newUserRequest){
        return userService.saveUser(newUserRequest);
    }
    
    @PostMapping
    public ResponseEntity<UserDto> loadUser(@RequestBody @Valid UserLoginRequest userLoginRequest){
        return ResponseEntity.ok().body(userService.loadUser(userLoginRequest));
    }
    
    @GetMapping("/all")
    public List<UserDto> getAll(){
        return userService.getAll();
    }
    
    @GetMapping("/{username}")
    public UserDto getOne(@PathVariable String username){
        return userService.getOne(username);
    }
    
//    @GetMapping("/token/refresh")
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
//
//    }
}
