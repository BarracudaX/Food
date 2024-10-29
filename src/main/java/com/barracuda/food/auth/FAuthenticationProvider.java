package com.barracuda.food.auth;

import com.barracuda.food.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public FAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var email = authentication.getPrincipal().toString();
        var password = authentication.getCredentials().toString();

        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email "+email+" not found."));

        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new BadCredentialsException("Invalid Credentials.");
        }

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().name()));

        return new FAuthenticationToken(user.getId(), "", authorities,user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
