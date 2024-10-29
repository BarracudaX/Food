package com.barracuda.food.controller;

import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.spring6.view.ThymeleafView;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    ModelAndView createUser(@ModelAttribute UserRegistrationForm registrationForm){
        try{
            userService.createUser(registrationForm);
        }catch (ConstraintViolationException ex){
            var errors = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
            return new ModelAndView("register", Map.of("errors",errors));
        }

        return new ModelAndView("redirect:login");
    }

}
