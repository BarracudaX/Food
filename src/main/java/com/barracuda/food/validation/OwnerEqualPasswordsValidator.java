package com.barracuda.food.validation;

import com.barracuda.food.dto.OwnerCreationForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class OwnerEqualPasswordsValidator implements ConstraintValidator<EqualPasswords, OwnerCreationForm> {

    @Override
    public boolean isValid(OwnerCreationForm value, ConstraintValidatorContext context) {
        if(value == null) return true;

        return switch (value.password()){
            case null -> value.repeatedPassword() == null;
            default -> value.password().equals(value.repeatedPassword());
        };
    }
}
