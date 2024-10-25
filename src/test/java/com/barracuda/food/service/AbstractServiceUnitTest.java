package com.barracuda.food.service;

import com.barracuda.food.AbstractTest;
import com.barracuda.food.infrastructure.ServiceTest;
import com.barracuda.food.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

@ServiceTest
public abstract class AbstractServiceUnitTest extends AbstractTest {

    @MockBean
    protected UserRepository userRepositoryMock;

    @MockBean
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected MessageSource messageSource;
}
