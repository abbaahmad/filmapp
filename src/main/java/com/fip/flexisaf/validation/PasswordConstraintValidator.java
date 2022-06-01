package com.fip.flexisaf.validation;

import com.google.common.base.Joiner;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    
    @Override
    public void initialize(final ValidPassword constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }
    
    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
        
        final PasswordValidator passwordValidator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
//                new UppercaseCharacterRule(1),
//                new DigitCharacterRule(1),
//                new SpecialCharacterRule(1),
//                new NumericalSequenceRule(3,false),
//                new AlphbeticalSequenceRule(3, false),
//                new QwertySequenceRule(3, false),
                new WhitespaceRule()
        ));
        final RuleResult result = passwordValidator.validate(new PasswordData(password));
        if(result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(Joiner.on(",").join(passwordValidator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }
}
