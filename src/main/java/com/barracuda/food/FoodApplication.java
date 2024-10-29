package com.barracuda.food;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FoodApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(FoodApplication.class, args);
    }

}
