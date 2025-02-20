package com.barracuda.food.auth;

import com.barracuda.food.entity.Admin;
import com.barracuda.food.entity.Manager;
import com.barracuda.food.entity.Owner;
import com.barracuda.food.entity.Staff;
import com.barracuda.food.entity.enums.Role;
import com.barracuda.food.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FAuthenticationProvider implements AuthenticationProvider, UserDetailsService {

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

        var role = switch (user){
            case Owner ignored -> "ROLE_" + Role.OWNER.name();
            case Admin ignored -> "ROLE_" + Role.ADMIN.name();
            case Manager ignored -> "ROLE_" + Role.MANAGER.name();
            case Staff ignored -> "ROLE_" + Role.STAFF.name();
            default -> "ROLE_" + Role.USER.name();
        };

        return new UsernamePasswordAuthenticationToken(user,"",List.of(new SimpleGrantedAuthority(role)));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User with email "+username+" not found."));
    }
}
