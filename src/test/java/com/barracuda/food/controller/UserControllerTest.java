package com.barracuda.food.controller;

import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @see com.barracuda.food.controller.UserController
 */
public class UserControllerTest extends AbstractControllerTest{

    private final UserService userServiceMock;

    private final UserRegistrationForm.UserRegistrationFormBuilder builder = UserRegistrationForm.builder()
            .name("SOME_NAME")
            .email("some@email.com")
            .password("SomePass123!")
            .repeatedPassword("SomePass123!");

    @Autowired
    public UserControllerTest(UserService userServiceMock) {
        this.userServiceMock = userServiceMock;
    }

    @Test
    void shouldCreateNewUserAndRedirectToLoginPage() throws Exception {
        var form = builder.build();
        prepareFormData(form)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login"));

        verify(userServiceMock).createUser(form);
    }

    @MethodSource("emptyStrings")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenNameIsEmpty(String emptyName) throws Exception {
        var formWithEmptyName = builder.name(emptyName).build();

        prepareFormData(formWithEmptyName)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","name"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("emptyStrings")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenEmailIsEmpty(String emptyEmail) throws Exception {
        var formWithEmptyEmail = builder.email(emptyEmail).build();
        prepareFormData(formWithEmptyEmail)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","email"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("invalidEmails")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenEmailIsInvalid(String invalidEmail) throws Exception {
        var formWithInvalidEmail = builder.email(invalidEmail).build();
        prepareFormData(formWithInvalidEmail)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","email"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("emptyStrings")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenPasswordIsEmpty(String emptyPassword) throws Exception {
        var formWithEmptyPassword = builder.password(emptyPassword).repeatedPassword(emptyPassword).build();
        prepareFormData(formWithEmptyPassword)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","password"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("invalidPasswords")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenPasswordIsInvalid(String invalidPassword) throws Exception {
        var formWithInvalidPassword = builder.password(invalidPassword).repeatedPassword(invalidPassword).build();

        prepareFormData(formWithInvalidPassword)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","password"));

        verifyNoInteractions(userServiceMock);
    }

    @Test
    void shouldReturnUserBackWithErrorWhenTwoPasswordsDoNotMatch() throws Exception {
        var formWithTwoNonMatchingPasswords = builder.repeatedPassword("DiffPass123!").build();

        prepareFormData(formWithTwoNonMatchingPasswords)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasErrors("form"));

        verifyNoInteractions(userServiceMock);
    }

    private ResultActions prepareFormData(UserRegistrationForm form) throws Exception {
        return mockMvc.perform(post("/user")
                .with(csrf())
                .param("name",form.getName())
                .param("email",form.getEmail())
                .param("password",form.getPassword())
                .param("repeatedPassword",form.getRepeatedPassword())
        );
    }
}
