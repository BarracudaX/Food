package com.barracuda.food.validation;

import com.barracuda.food.dto.UserRegistrationForm;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Component
public class EqualPasswordsValidator implements ConstraintValidator<EqualPasswordsValidator.EqualPasswords, UserRegistrationForm> {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Constraint(validatedBy = {EqualPasswordsValidator.class})
    public @interface EqualPasswords{

        String message() default "com.barracuda.food.validation.EqualPasswords.message";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Override
    public boolean isValid(UserRegistrationForm value, ConstraintValidatorContext context) {
        if(value == null) return true;

        return (value.password() == null && value.repeatedPassword() == null) || value.password().equals(value.repeatedPassword());
    }
}
