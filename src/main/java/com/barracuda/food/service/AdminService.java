package com.barracuda.food.service;

import com.barracuda.food.dto.OwnerCreationForm;
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

    public Owner createOwner(OwnerCreationForm form){
        var owner = new Owner(form.name(),form.email(),passwordEncoder.encode(form.password()));

        return userRepository.save(owner);
    }

}
