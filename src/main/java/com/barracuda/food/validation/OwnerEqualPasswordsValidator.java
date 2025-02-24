package com.barracuda.food.validation;

import com.barracuda.food.dto.OwnerCreateForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class OwnerEqualPasswordsValidator implements ConstraintValidator<EqualPasswords, OwnerCreateForm> {

    @Override
    public boolean isValid(OwnerCreateForm value, ConstraintValidatorContext context) {
        if(value == null) return true;

        return switch (value.getPassword()){
            case null -> value.getRepeatedPassword() == null;
            default -> value.getPassword().equals(value.getRepeatedPassword());
        };
    }
}
