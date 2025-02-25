package com.barracuda.food.validation;

import com.barracuda.food.dto.UserCreateForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UserEqualPasswordsValidator implements ConstraintValidator<EqualPasswords, UserCreateForm> {

    @Override
    public boolean isValid(UserCreateForm value, ConstraintValidatorContext context) {
        if(value == null) return true;

        return switch (value.getPassword()){
            case null -> value.getRepeatedPassword() == null;
            default -> value.getPassword().equals(value.getRepeatedPassword());
        };
    }
}
