package com.barracuda.food.infrastructure;

import com.barracuda.food.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class FSecurityContextFactory implements WithSecurityContextFactory<WithFUser> {

    @Override
    public SecurityContext createSecurityContext(WithFUser annotation) {
        var user = new User(1L,"test","test","test");
        var authentication = new UsernamePasswordAuthenticationToken(user,"", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        return securityContext;
    }

}
