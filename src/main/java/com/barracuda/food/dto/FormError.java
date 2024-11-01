package com.barracuda.food.dto;

public sealed interface FormError {

    String message();

    record FieldFormError(String fieldName,String message) implements FormError{

    }

    record GlobalFormError(String message) implements FormError{

    }

}
