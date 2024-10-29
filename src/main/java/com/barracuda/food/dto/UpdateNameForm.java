package com.barracuda.food.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateNameForm(
        @NotBlank(message = "{User.name.NotBlank.message}")
        String name,

        Long id
) { }
