package com.fip.flexisaf.controllers.requests.user;

import com.fip.flexisaf.models.Role;
import com.fip.flexisaf.validation.PasswordMatches;
import com.fip.flexisaf.validation.ValidEmail;
import com.fip.flexisaf.validation.ValidPassword;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@PasswordMatches
public class NewUserRequest {
    @NotBlank(message = "Name cannot be blank!")
    private String name;
    
    @NotBlank(message = "email cannot be empty.")
    @ValidEmail
    private String email;
    
    @NotBlank(message = "password cannot be blank!")
    @ValidPassword
    private String password;
    
    @NotNull(message = "password cannot be null!")
    @ValidPassword
    private String matchingPassword;
    
    private Role role;
}
