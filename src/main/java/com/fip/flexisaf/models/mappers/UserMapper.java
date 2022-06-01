package com.fip.flexisaf.models.mappers;

import com.fip.flexisaf.controllers.requests.user.NewUserRequest;
import com.fip.flexisaf.models.User;
import com.fip.flexisaf.models.dto.UserDto;

public class UserMapper {
    public static User toUser(NewUserRequest userRequest){
        return new User()
                .setEmail(userRequest.getEmail())
                .setName(userRequest.getName())
                .setRole(userRequest.getRole())
                .setEnabled(true);
    }
    public static UserDto toUserDto(User user){
        return new UserDto()
                .setEmail(user.getUsername())
                .setName(user.getName())
                .setRole(user.getRole());
    }
}
