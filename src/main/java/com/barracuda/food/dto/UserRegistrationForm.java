package com.barracuda.food.dto;

import com.barracuda.food.entity.User;
import com.barracuda.food.validation.EqualPasswordsValidator.EqualPasswords;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@EqualPasswords(message = "{UserRegistrationForm.EqualPasswords.message}")
public record UserRegistrationForm(
        @NotBlank(message = "{UserRegistrationForm.name.NotBlank.message}")
        String name,

        @NotBlank(message = "{UserRegistrationForm.email.Email.message}")
        @Email(message = "{UserRegistrationForm.email.Email.message}")
        String email,

        @NotBlank(message = "{UserRegistrationForm.password.NotBlank.message}")
        @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{8,}$",message = "{UserRegistrationForm.password.Pattern.message}")
        String password,

        String repeatedPassword
) {

        public static class Builder {

                private String name;
                private String email;
                private String password;
                private String repeatedPassword;

                public Builder(){

                }

                public Builder(UserRegistrationForm source){
                        this.name = source.name;
                        this.email = source.email;
                        this.password = source.password;
                        this.repeatedPassword = source.repeatedPassword;
                }


                public Builder name(String name){
                        this.name = name;
                        return this;
                }

                public Builder email(String email){
                        this.email = email;
                        return this;
                }

                public Builder password(String password){
                        this.password = password;
                        return this;
                }

                public Builder repeatedPassword(String repeatedPassword){
                        this.repeatedPassword = repeatedPassword;
                        return this;
                }

                public UserRegistrationForm build(){
                        return new UserRegistrationForm(name,email,password,repeatedPassword);
                }

        }

}
