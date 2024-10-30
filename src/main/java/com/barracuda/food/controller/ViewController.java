package com.barracuda.food.controller;

import com.barracuda.food.auth.FAuthenticationToken;
import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.dto.UserRegistrationForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class ViewController {

    @GetMapping("/")
    String homePage(){
        return "home";
    }

    @GetMapping("/login")
    String loginPage(){
        return "login" ;
    }

    @GetMapping({"/register","/user"})
    String registerPage(Model model){
        model.addAttribute("form",UserRegistrationForm.builder().build());
        return "register";
    }

    @GetMapping("/profile")
    String profilePage(Model model, Principal principal){
        var authentication = (FAuthenticationToken) principal;
        model.addAttribute("nameForm",new UpdateNameForm(authentication.getUser().getName(),null));
        return "profile";
    }
}
