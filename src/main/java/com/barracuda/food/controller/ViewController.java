package com.barracuda.food.controller;

import com.barracuda.food.auth.FAuthenticationToken;
import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.entity.User;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ViewController {


    @GetMapping("/")
    String homePage(){
        return "home";
    }

    @GetMapping("/login")
    String loginPage(@RequestParam(name = "ott",required = false) String ott, @RequestParam(name = "ottFailed", required = false) String ottFailed,Model model){
        if(ott != null){
            model.addAttribute("success","OTT Generated! Check Out Logs!");
        }

        if( ottFailed != null){
            model.addAttribute("errors","OTT Authentication Failed!");
        }

        return "login" ;
    }

    @GetMapping("/ott")
    String ottLogin(){
        return "ott";
    }

    @GetMapping({"/register","/user"})
    String registerPage(Model model){
        model.addAttribute("form",UserRegistrationForm.builder().build());
        return "register";
    }

    @GetMapping("/profile")
    String profilePage(Model model, Principal principal){
        var user = switch (principal){
            case OneTimeTokenAuthenticationToken o -> (User) o.getPrincipal();
            case FAuthenticationToken f -> f.getUser();
            default -> throw new IllegalStateException("Unknown principal "+principal);
        };

        model.addAttribute("nameForm",new UpdateNameForm(user.getName(),null));
        model.addAttribute("email",user.getEmail());
        return "profile";
    }

}
