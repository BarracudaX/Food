package com.barracuda.food.configuration;

import com.barracuda.food.controller.ViewController;
import com.barracuda.food.entity.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.ott.JdbcOneTimeTokenService;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;

import java.util.List;

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
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,OneTimeTokenService oneTimeTokenService) throws Exception {
        return httpSecurity
                .anonymous(Customizer.withDefaults())
                .formLogin(form ->{
                    form.loginPage("/login");
                })
                .logout(logout ->{
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login");
                }).oneTimeTokenLogin( ott -> {
                    ott.tokenService(oneTimeTokenService);
                })
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(HttpMethod.GET,"/","/login","/register","/user","/error","/ott").permitAll()
                            .requestMatchers(HttpMethod.POST,"/user","/ott/generate","/login/ott").permitAll()
                            .requestMatchers(HttpMethod.GET,"/profile").hasRole(Role.USER.name())
                            .requestMatchers(HttpMethod.POST,"/user/name").hasRole(Role.USER.name())
                            .requestMatchers("/restaurants").hasRole(Role.OWNER.name())
                            .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                            .requestMatchers(HttpMethod.GET,"/resources/**").permitAll()
                            .anyRequest().denyAll();
                })
                .build();
    }


    @Bean
    OneTimeTokenGenerationSuccessHandler oneTimeTokenGenerationSuccessHandler(){
        return (_, resp, oneTimeToken) -> {
            resp.sendRedirect("/login?ott=true");
            System.out.println(oneTimeToken.getTokenValue());
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    OneTimeTokenService oneTimeTokenService(JdbcOperations jdbcOperations){
        return new JdbcOneTimeTokenService(jdbcOperations);
    }
}
