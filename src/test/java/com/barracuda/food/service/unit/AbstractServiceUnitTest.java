package com.barracuda.food.service.unit;

import com.barracuda.food.AbstractTest;
import com.barracuda.food.infrastructure.ServiceTest;
import com.barracuda.food.repository.OwnerRepository;
import com.barracuda.food.repository.RestaurantRepository;
import com.barracuda.food.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ServiceTest
public abstract class AbstractServiceUnitTest extends AbstractTest {

    @MockitoBean
    protected UserRepository userRepositoryMock;

    @MockitoBean
    protected RestaurantRepository restaurantRepositoryMock;

    @MockitoBean
    protected OwnerRepository ownerRepositoryMock;

    @MockitoBean
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected MessageSource messageSource;
}
