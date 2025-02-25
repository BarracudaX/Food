package com.barracuda.food.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = {UserEqualPasswordsValidator.class})
public @interface EqualPasswords {

    String message() default "com.barracuda.food.validation.EqualPasswords.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
