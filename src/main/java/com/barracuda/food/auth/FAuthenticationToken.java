package com.barracuda.food.auth;

import com.barracuda.food.entity.User;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class FAuthenticationToken extends UsernamePasswordAuthenticationToken {

    @Getter
    private User user;

    public FAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, User user) {
        super(principal, credentials, authorities);
        this.user = user;
        setDetails(user);
    }

    public void setUser(User user) {
        this.user = user;
        setDetails(user);
    }
}
