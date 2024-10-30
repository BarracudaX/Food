package com.barracuda.food.service;

import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.infrastructure.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ServiceTest
public class UserServiceTest extends AbstractServiceUnitTest {

    private final UserRegistrationForm registrationForm = UserRegistrationForm.builder().name("SOME_NAME").email("SOME@EMAIL.COM").password("SomePass123").repeatedPassword("SomePass123").build();
    private final User user = new User(registrationForm.getName(),registrationForm.getEmail(),"ENCODED_PASSWORD");
    private final UserService userService;

    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(registrationForm.getPassword())).thenReturn(user.getPassword());
        when(userRepositoryMock.save(any())).thenReturn(user);
    }

    @Test
    void shouldCreateNewUser() {
        var savedUser = ArgumentCaptor.forClass(User.class);
        assertThat(userService.createUser(registrationForm)).isSameAs(user);

        verify(passwordEncoder).encode(registrationForm.getPassword());
        verify(userRepositoryMock).save(savedUser.capture());
        assertThat(savedUser.getValue()).usingRecursiveComparison().isEqualTo(user);
    }

}
