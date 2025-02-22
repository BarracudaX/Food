package com.barracuda.food.service;

import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.infrastructure.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ServiceTest
public class UserServiceUnitTest extends AbstractServiceUnitTest {

    private final UserRegistrationForm registrationForm = UserRegistrationForm.builder().name("SOME_NAME").email("SOME@EMAIL.COM").password("SomePass123").repeatedPassword("SomePass123").build();
    private final User user = new User(1L,registrationForm.getName(),registrationForm.getEmail(),"ENCODED_PASSWORD");
    private final UserServiceImpl userService;
    private final UpdateNameForm form = new UpdateNameForm("NewName",user.getId());

    public UserServiceUnitTest(UserServiceImpl userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(registrationForm.getPassword())).thenReturn(user.getPassword());
        when(userRepositoryMock.save(any())).thenReturn(user);
        when(userRepositoryMock.findById(user.getId())).thenReturn(Optional.of(user));
    }

    @Test
    void shouldCreateNewUser() {
        var savedUser = ArgumentCaptor.forClass(User.class);
        assertThat(userService.createUser(registrationForm)).isSameAs(user);

        verify(passwordEncoder).encode(registrationForm.getPassword());
        verify(userRepositoryMock).save(savedUser.capture());
        assertThat(savedUser.getValue()).usingRecursiveComparison().ignoringFields("id").isEqualTo(user);
    }

    @Test
    void shouldUpdateUserName() {
        assertThat(user.getName()).isNotEqualTo(form.getName());

        userService.changeUserName(form);

        assertThat(user.getName()).isEqualTo(form.getName());
    }

    @Test
    void shouldThrowEmptyResultDataAccessExceptionWhenTryingToUpdateNonExistingUser() {
        when(userRepositoryMock.findById(form.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.changeUserName(form)).isInstanceOf(EmptyResultDataAccessException.class);
    }
}
