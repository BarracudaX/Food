package com.barracuda.food.service;

import com.barracuda.food.dto.UserCreateForm;
import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserCreateForm registrationForm){
        var user = new User(registrationForm.getName(),registrationForm.getEmail(),passwordEncoder.encode(registrationForm.getPassword()));

        return userRepository.save(user);
    }

    public Page<User> users(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public User changeUserName(@Valid UpdateNameForm updateForm){
        return userRepository
                .findById(updateForm.getId())
                .map(user -> {
                    user.setName(updateForm.getName());
                    return user;
                }).orElseThrow(() ->  new EmptyResultDataAccessException(1) );
    }


}
