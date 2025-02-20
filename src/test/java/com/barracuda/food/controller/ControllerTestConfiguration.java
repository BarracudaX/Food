package com.barracuda.food.controller;

import com.barracuda.food.entity.User;
import com.barracuda.food.infrastructure.FSecurityContextFactory;
import com.barracuda.food.infrastructure.WithFUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@Configuration
public class ControllerTestConfiguration {

    @Bean
    public WithSecurityContextFactory<WithFUser> withSecurityContextFactory(){
        return new FSecurityContextFactory();
    }

}
