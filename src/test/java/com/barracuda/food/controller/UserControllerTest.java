package com.barracuda.food.controller;

import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.infrastructure.WithFUser;
import com.barracuda.food.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

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

    private final UpdateNameForm.UpdateNameFormBuilder updateNameFormBuilder = UpdateNameForm.builder().name("test").id(1L);

    public UserControllerTest(UserService userServiceMock) {
        this.userServiceMock = userServiceMock;
    }

    @Test
    void shouldCreateNewUserAndRedirectToLoginPage() throws Exception {
        var form = builder.build();
        prepareCreateForm(form)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login"));

        verify(userServiceMock).createUser(form);
    }

    @MethodSource("emptyStrings")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenNameIsEmpty(String emptyName) throws Exception {
        var formWithEmptyName = builder.name(emptyName).build();

        prepareCreateForm(formWithEmptyName)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","name"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("emptyStrings")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenEmailIsEmpty(String emptyEmail) throws Exception {
        var formWithEmptyEmail = builder.email(emptyEmail).build();
        prepareCreateForm(formWithEmptyEmail)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","email"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("invalidEmails")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenEmailIsInvalid(String invalidEmail) throws Exception {
        var formWithInvalidEmail = builder.email(invalidEmail).build();
        prepareCreateForm(formWithInvalidEmail)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","email"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("emptyStrings")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenPasswordIsEmpty(String emptyPassword) throws Exception {
        var formWithEmptyPassword = builder.password(emptyPassword).repeatedPassword(emptyPassword).build();
        prepareCreateForm(formWithEmptyPassword)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","password"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("invalidPasswords")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenPasswordIsInvalid(String invalidPassword) throws Exception {
        var formWithInvalidPassword = builder.password(invalidPassword).repeatedPassword(invalidPassword).build();

        prepareCreateForm(formWithInvalidPassword)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","password"));

        verifyNoInteractions(userServiceMock);
    }

    @Test
    void shouldReturnUserBackWithErrorWhenTwoPasswordsDoNotMatch() throws Exception {
        var formWithTwoNonMatchingPasswords = builder.repeatedPassword("DiffPass123!").build();

        prepareCreateForm(formWithTwoNonMatchingPasswords)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasErrors("form"));

        verifyNoInteractions(userServiceMock);
    }

    @WithFUser
    @Test
    void shouldUpdateUserNameAndReturnToProfile() throws Exception {
        var form = updateNameFormBuilder.build();

        prepareNameUpdateForm(form)
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("success"));

        verify(userServiceMock).changeUserName(form);
    }

    @MethodSource("emptyStrings")
    @ParameterizedTest
    @WithFUser
    void shouldReturnUserBackWithErrorWhenUpdatingNameWithEmptyString(String emptyString) throws Exception {
        var form = updateNameFormBuilder.name(emptyString).build();

        prepareNameUpdateForm(form)
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeHasFieldErrors("nameForm","name"));

        verifyNoInteractions(userServiceMock);
    }

    private ResultActions prepareCreateForm(UserRegistrationForm form) throws Exception {
        return mockMvc.perform(post("/user")
                .with(csrf())
                .param("name",form.getName())
                .param("email",form.getEmail())
                .param("password",form.getPassword())
                .param("repeatedPassword",form.getRepeatedPassword())
        );
    }

    private ResultActions prepareNameUpdateForm(UpdateNameForm updateNameForm) throws Exception {
        return mockMvc.perform(post("/user/name")
                .with(csrf())
                .param("name",updateNameForm.getName())
                .param("id",updateNameForm.getId().toString())
        );
    }
}
