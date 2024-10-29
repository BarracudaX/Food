package com.barracuda.food.controller;

import com.barracuda.food.auth.FAuthenticationToken;
import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.spring6.view.ThymeleafView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MessageSource messageSource;

    public UserController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
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

    @PostMapping("/name")
    ModelAndView updateName(@RequestParam("name") String name, Principal principal){
        var authentication = (FAuthenticationToken) principal;
        var successMsg = new ArrayList<String>();
        var errors = new ArrayList<String>();
        try{
            var updateForm = new UpdateNameForm(name,authentication.getUser().getId());
            updateSecurityContext(userService.changeUserName(updateForm),authentication);
            successMsg.add(messageSource.getMessage("success.name.update.message",new Object[0], LocaleContextHolder.getLocale()));
        }catch (ConstraintViolationException ex){
            errors.addAll(ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList());
        }

        return new ModelAndView("profile",Map.of("errors",errors,"success",successMsg));
    }

    private void updateSecurityContext(User user,FAuthenticationToken authentication){
        var newSecurityContext = SecurityContextHolder.createEmptyContext();
        authentication.setUser(user);
        newSecurityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(newSecurityContext);
    }

}
