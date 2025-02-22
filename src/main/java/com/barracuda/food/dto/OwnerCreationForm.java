package com.barracuda.food.dto;

import com.barracuda.food.validation.EqualPasswords;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@EqualPasswords(message = "{User.EqualPasswords.message}")
public record OwnerCreationForm(
        @NotBlank(message = "{User.name.NotBlank.message}")
        String name,

        @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{8,}$", message = "{User.password.Pattern.message}")
        @NotBlank(message = "{User.password.NotBlank.message}")
        String password,

        String repeatedPassword,

        @Email(message = "{User.email.Email.message}")
        @NotBlank(message = "{User.email.Email.message}")
        String email
) {
}
