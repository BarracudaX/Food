package com.barracuda.food.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public final class UpdateNameForm {

    @NotBlank(message = "{User.name.NotBlank.message}")
    private String name;

    private Long id;
}
