package com.barracuda.food.service;

import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(@Valid UserRegistrationForm registrationForm){
        var user = new User(registrationForm.name(),registrationForm.email(),passwordEncoder.encode(registrationForm.password()));

        return userRepository.save(user);
    }

    public User changeUserName(@Valid UpdateNameForm updateForm){
        return userRepository
                .findById(updateForm.id())
                .map(user -> {
                    user.setName(updateForm.name());
                    return user;
                }).orElseThrow(() ->  new EmptyResultDataAccessException(1) );
    }


}
