package com.barracuda.food.controller;

import com.barracuda.food.dto.CreateUserForm;
import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.service.RestaurantService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    private final RestaurantService restaurantService;

    public ViewController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

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
        model.addAttribute("form", new CreateUserForm("","","",""));
        return "register";
    }

    @GetMapping("/profile")
    String profilePage(Model model, Authentication authentication){
        var user = (User) authentication.getPrincipal();

        model.addAttribute("nameForm",new UpdateNameForm(user.getName(),null));
        model.addAttribute("email",user.getEmail());
        return "profile";
    }

}
