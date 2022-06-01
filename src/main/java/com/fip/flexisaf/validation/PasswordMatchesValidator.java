package com.fip.flexisaf.validation;

import com.fip.flexisaf.models.User;
import com.fip.flexisaf.models.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }
    
    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        final UserDto user = (UserDto) value;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
