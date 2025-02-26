package com.barracuda.food.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;

@Embeddable
public record Address(

        @NotEmpty(message = "{Address.city.NotEmpty.message}")
        String city,

        @NotEmpty(message = "{Address.street.NotEmpty.message}")
        String street
) { }
