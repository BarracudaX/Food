package com.barracuda.food.controller;

import com.barracuda.food.service.AdminService;
import com.barracuda.food.service.UserService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerTestConfiguration {

    @MockBean
    public AdminService adminService;

    @MockBean
    public UserService userService;

}
