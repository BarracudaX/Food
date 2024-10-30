package com.barracuda.food.dto;

import com.barracuda.food.entity.User;
import com.barracuda.food.validation.EqualPasswordsValidator.EqualPasswords;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@EqualPasswords(message = "{User.EqualPasswords.message}")
@Data
@Builder
public final class UserRegistrationForm {

    @NotBlank(message = "{User.name.NotBlank.message}")
    private String name;

    @Email(message = "{User.email.Email.message}")
    @NotBlank(message = "{User.email.Email.message}")
    private String email;

    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{8,}$", message = "{User.password.Pattern.message}")
    @NotBlank(message = "{User.password.NotBlank.message}")
    private String password;

    private String repeatedPassword;
}
