package com.fip.flexisaf.controllers.requests.user;

import com.fip.flexisaf.validation.ValidEmail;
import com.fip.flexisaf.validation.ValidPassword;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
public class UserLoginRequest {
    @ValidEmail
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    
    @ValidPassword
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
