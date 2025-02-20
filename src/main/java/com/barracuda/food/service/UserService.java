package com.barracuda.food.service;

import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User createUser(UserRegistrationForm registrationForm);

    Page<User> users(Pageable pageable);

    User changeUserName(@Valid UpdateNameForm updateForm);
}
