package com.barracuda.food.controller;

import com.barracuda.food.dto.UserCreateForm;
import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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
    ModelAndView createUser(@Validated @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("register");
        }

        userService.createUser(form);

        return new ModelAndView("redirect:login");
    }

    @PostMapping("/name")
    ModelAndView updateName(@ModelAttribute("nameForm") @Valid UpdateNameForm form, BindingResult bindingResult, Authentication authentication){
        if(bindingResult.hasErrors()){
            return new ModelAndView("profile");
        }
        var userID = WebUtils.getUserID();

        form.setId(userID);
        var user = userService.changeUserName(form);

        WebUtils.updateSecurityContext(user,authentication);

        var successMsg = new ArrayList<String>();
        successMsg.add(messageSource.getMessage("success.name.update.message",new Object[0], LocaleContextHolder.getLocale()));

        return new ModelAndView("profile",Map.of("success",successMsg));
    }



}
