package com.barracuda.food.controller;

import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
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
    ModelAndView createUser(@Validated @ModelAttribute("form") UserRegistrationForm form, BindingResult bindingResult) {
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
        var userID = ((User) authentication.getPrincipal()).getId();

        form.setId(userID);
        var user = userService.changeUserName(form);

        updateSecurityContext(user,authentication);

        var successMsg = new ArrayList<String>();
        successMsg.add(messageSource.getMessage("success.name.update.message",new Object[0], LocaleContextHolder.getLocale()));

        return new ModelAndView("profile",Map.of("success",successMsg));
    }

    private void updateSecurityContext(User user,Authentication current){
        var newContext = SecurityContextHolder.createEmptyContext();

        var newAuthentication = switch (current){
            case OneTimeTokenAuthenticationToken _ -> new OneTimeTokenAuthenticationToken(user,current.getAuthorities());
            case UsernamePasswordAuthenticationToken _ -> new UsernamePasswordAuthenticationToken(user,"",current.getAuthorities());
            default -> throw new IllegalStateException("Unknown authentication "+current);
        };

        newContext.setAuthentication(newAuthentication);
        SecurityContextHolder.setContext(newContext);
    }

}
