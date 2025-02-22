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

    private final UserRegistrationForm form = new UserRegistrationForm("SOME_NAME","SOME@EMAIL.COM","SomePass123!","SomePass123!");
    private final User user = new User(1L, form.name(), form.email(),"ENCODED_PASSWORD");
    private final UserServiceImpl userService;
    private final UpdateNameForm updateForm = new UpdateNameForm("NewName",user.getId());

    public UserServiceUnitTest(UserServiceImpl userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(form.password())).thenReturn(user.getPassword());
        when(userRepositoryMock.save(any())).thenReturn(user);
        when(userRepositoryMock.findById(user.getId())).thenReturn(Optional.of(user));
    }

    @Test
    void shouldCreateNewUser() {
        var savedUser = ArgumentCaptor.forClass(User.class);
        assertThat(userService.createUser(form)).isSameAs(user);

        verify(passwordEncoder).encode(form.password());
        verify(userRepositoryMock).save(savedUser.capture());
        assertThat(savedUser.getValue()).usingRecursiveComparison().ignoringFields("id").isEqualTo(user);
    }

    @Test
    void shouldUpdateUserName() {
        assertThat(user.getName()).isNotEqualTo(updateForm.getName());

        userService.changeUserName(updateForm);

        assertThat(user.getName()).isEqualTo(updateForm.getName());
    }

    @Test
    void shouldThrowEmptyResultDataAccessExceptionWhenTryingToUpdateNonExistingUser() {
        when(userRepositoryMock.findById(updateForm.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.changeUserName(updateForm)).isInstanceOf(EmptyResultDataAccessException.class);
    }
}
