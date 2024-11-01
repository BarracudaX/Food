package com.barracuda.food.validation;

import com.barracuda.food.dto.UserRegistrationForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UserEqualPasswordsValidator implements ConstraintValidator<EqualPasswords, UserRegistrationForm> {

    @Override
    public boolean isValid(UserRegistrationForm value, ConstraintValidatorContext context) {
        if(value == null) return true;

        return (value.getPassword() == null && value.getRepeatedPassword() == null) || value.getPassword().equals(value.getRepeatedPassword());
    }
}
