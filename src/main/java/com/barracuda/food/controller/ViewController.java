package com.barracuda.food.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
    String registerPage(){ return "register"; }
}
