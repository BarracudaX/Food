package com.barracuda.food.service;

import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.infrastructure.ServiceTest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ServiceTest
public class UserServiceTest extends AbstractServiceUnitTest {

    private final UserRegistrationForm registrationForm = new UserRegistrationForm("SOME_NAME","SOME@EMAIL.COM","SomePass123","SomePass123");
    private final User user = new User(registrationForm.name(),registrationForm.email(),"ENCODED_PASSWORD");
    private final UserService userService;

    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(registrationForm.password())).thenReturn(user.getPassword());
        when(userRepositoryMock.save(any())).thenReturn(user);
    }

    @ParameterizedTest
    @MethodSource("emptyStrings")
    void shouldThrowConstraintValidationExceptionWhenRegisteringWithEmptyName(String empty) {
        var invalidRegistrationForm = new UserRegistrationForm.Builder(registrationForm).name(empty).build();

        assertThatThrownBy(() -> userService.createUser(invalidRegistrationForm))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining(messageSource.getMessage("UserRegistrationForm.name.NotBlank.message", new Object[]{}, LocaleContextHolder.getLocale()));

        verifyNoInteractions(userRepositoryMock,passwordEncoder);
    }

    @ParameterizedTest
    @MethodSource("invalidEmails")
    void shouldThrowConstraintValidationExceptionWhenRegisteringWithInvalidEmail(String invalidEmail) {
        var invalidRegistrationForm = new UserRegistrationForm.Builder(registrationForm).email(invalidEmail).build();

        assertThatThrownBy(() -> userService.createUser(invalidRegistrationForm))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining(messageSource.getMessage("UserRegistrationForm.email.Email.message",new Object[]{},LocaleContextHolder.getLocale()));

        verifyNoInteractions(userRepositoryMock,passwordEncoder);
    }

    @ParameterizedTest
    @MethodSource("emptyStrings")
    void shouldThrowConstraintValidationExceptionWhenRegisteringWithEmptyEmail(String emptyString) {
        var invalidRegistrationForm = new UserRegistrationForm.Builder(registrationForm).email(emptyString).build();

        assertThatThrownBy(() -> userService.createUser(invalidRegistrationForm))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining(messageSource.getMessage("UserRegistrationForm.email.Email.message",new Object[]{},LocaleContextHolder.getLocale()));

        verifyNoInteractions(userRepositoryMock,passwordEncoder);
    }

    @ParameterizedTest
    @MethodSource("invalidPasswords")
    void shouldThrowConstraintValidationExceptionWhenRegisteringWithInvalidPassword(String invalidPassword) {
        var invalidRegistrationForm = new UserRegistrationForm.Builder(registrationForm).password(invalidPassword).repeatedPassword(invalidPassword).build();

        assertThatThrownBy(() -> userService.createUser(invalidRegistrationForm))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining(messageSource.getMessage("UserRegistrationForm.password.Pattern.message",new Object[]{},LocaleContextHolder.getLocale()));

        verifyNoInteractions(userRepositoryMock,passwordEncoder);
    }

    @ParameterizedTest
    @MethodSource("emptyStrings")
    void shouldThrowConstraintValidationExceptionWhenRegisteringWithBlankPassword(String emptyPassword) {
        var invalidRegistrationForm = new UserRegistrationForm.Builder(registrationForm).password(emptyPassword).repeatedPassword(emptyPassword).build();

        assertThatThrownBy(() -> userService.createUser(invalidRegistrationForm))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining(messageSource.getMessage("UserRegistrationForm.password.NotBlank.message",new Object[]{},LocaleContextHolder.getLocale()));

        verifyNoInteractions(userRepositoryMock,passwordEncoder);
    }

    @Test
    void shouldThrowConstraintValidationExceptionWhenRegisteringWithNotMatchingPassword() {
        var invalidRegistrationForm = new UserRegistrationForm.Builder(registrationForm).repeatedPassword(registrationForm.repeatedPassword() + "1").build();

        assertThatThrownBy(() -> userService.createUser(invalidRegistrationForm))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining(messageSource.getMessage("UserRegistrationForm.EqualPasswords.message",new Object[]{},LocaleContextHolder.getLocale()));

        verifyNoInteractions(userRepositoryMock,passwordEncoder);
    }

    @Test
    void shouldCreateNewUser() {
        var savedUser = ArgumentCaptor.forClass(User.class);
        assertThat(userService.createUser(registrationForm)).isSameAs(user);

        verify(passwordEncoder).encode(registrationForm.password());
        verify(userRepositoryMock).save(savedUser.capture());
        assertThat(savedUser.getValue()).usingRecursiveComparison().isEqualTo(user);
    }
}
