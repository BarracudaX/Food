package com.barracuda.food.dto;

import com.barracuda.food.validation.EqualPasswords;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualPasswords(message = "{User.EqualPasswords.message}")
@NoArgsConstructor
@ToString
@Data
public final class UserCreateForm {
        @NotBlank(message = "{User.name.NotBlank.message}")
        private String name;

        @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{8,}$", message = "{User.password.Pattern.message}")
        @NotBlank(message = "{User.password.NotBlank.message}")
        private String password;

        private String repeatedPassword;

        @Email(message = "{User.email.Email.message}")
        @NotBlank(message = "{User.email.Email.message}")
        private String email;

    public UserCreateForm(String name, String password, String repeatedPassword, String email) {
        this.name = name;
        this.password = password;
        this.repeatedPassword = repeatedPassword;
        this.email = email;
    }



}
