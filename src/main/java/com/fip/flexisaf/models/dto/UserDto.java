package com.fip.flexisaf.models.dto;

import com.fip.flexisaf.models.Role;
import com.fip.flexisaf.validation.PasswordMatches;
import com.fip.flexisaf.validation.ValidEmail;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@PasswordMatches
public class UserDto {
    //private String id;
    
    @NotNull
    private String name;
    
    @ValidEmail
    @NotNull
    private String email;
    
    @NotNull
    private String password;
    
    @NotNull
    private String matchingPassword;
    
    private Role role;
}
