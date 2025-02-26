package com.barracuda.food.validation;

import com.barracuda.food.dto.CreateUserForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UserEqualPasswordsValidator implements ConstraintValidator<EqualPasswords, CreateUserForm> {

    @Override
    public boolean isValid(CreateUserForm value, ConstraintValidatorContext context) {
        if(value == null) return true;

        return switch (value.getPassword()){
            case null -> value.getRepeatedPassword() == null;
            default -> value.getPassword().equals(value.getRepeatedPassword());
        };
    }
}
