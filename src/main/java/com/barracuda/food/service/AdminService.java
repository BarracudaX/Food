package com.barracuda.food.service;

import com.barracuda.food.dto.UserCreateForm;
import com.barracuda.food.entity.Owner;
import com.barracuda.food.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AdminService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Owner createOwner(UserCreateForm form){
        var owner = new Owner(form.getName(),form.getEmail(),passwordEncoder.encode(form.getPassword()));

        return userRepository.save(owner);
    }

}
