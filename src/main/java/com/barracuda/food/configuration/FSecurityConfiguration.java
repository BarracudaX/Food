package com.barracuda.food.configuration;

import com.barracuda.food.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class FSecurityConfiguration {

    @Bean
    RoleHierarchy roleHierarchy(){
        return RoleHierarchyImpl
                .withDefaultRolePrefix()
                .role(Role.ADMIN.name()).implies(Role.USER.name())
                .role(Role.OWNER.name()).implies(Role.USER.name(),Role.MANAGER.name(),Role.STAFF.name())
                .role(Role.MANAGER.name()).implies(Role.USER.name(),Role.STAFF.name())
                .role(Role.STAFF.name()).implies(Role.USER.name())
                .build();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .anonymous(Customizer.withDefaults())
                .formLogin(form ->{
                    form.loginPage("/login");
                })
                .logout(logout ->{
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login");
                })
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(HttpMethod.GET,"/","/login","/register","/user").permitAll()
                            .requestMatchers(HttpMethod.POST,"/user").permitAll()
                            .requestMatchers(HttpMethod.GET,"/profile").hasRole(Role.USER.name())
                            .requestMatchers(HttpMethod.POST,"/user/name").hasRole(Role.USER.name())
                            .anyRequest().denyAll();
                })
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
