package com.barracuda.food.controller;

import com.barracuda.food.auth.FAuthenticationToken;
import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
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

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.setAllowedFields("name","email","password","repeatedPassword");
    }

    @PostMapping
    ModelAndView createUser(@Validated @ModelAttribute("form") UserRegistrationForm form, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return new ModelAndView("register");
        }

        userService.createUser(form);

        return new ModelAndView("redirect:login");
    }

    @PostMapping("/name")
    ModelAndView updateName(@ModelAttribute("nameForm") @Valid UpdateNameForm form,BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return new ModelAndView("profile");
        }

        var authentication = (FAuthenticationToken) principal;
        var successMsg = new ArrayList<String>();
        form.setId(authentication.getUser().getId());

        updateSecurityContext(userService.changeUserName(form),authentication);

        successMsg.add(messageSource.getMessage("success.name.update.message",new Object[0], LocaleContextHolder.getLocale()));

        return new ModelAndView("profile",Map.of("success",successMsg));
    }

    private void updateSecurityContext(User user,FAuthenticationToken authentication){
        var newSecurityContext = SecurityContextHolder.createEmptyContext();
        authentication.setUser(user);
        newSecurityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(newSecurityContext);
    }

}
