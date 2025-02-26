package com.barracuda.food.controller;

import com.barracuda.food.dto.CreateUserForm;
import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.infrastructure.WithFUser;
import com.barracuda.food.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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

    private final CreateUserForm form = new CreateUserForm("SOME_NAME","SomePass123!", "SomePass123!","some@email.com");

    private final UpdateNameForm.UpdateNameFormBuilder updateNameFormBuilder = UpdateNameForm.builder().name("test").id(1L);

    public UserControllerTest(UserService userServiceMock) {
        this.userServiceMock = userServiceMock;
    }

    @Test
    void shouldCreateNewUserAndRedirectToLoginPage() throws Exception {
        prepareCreateForm(form)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login"));

        verify(userServiceMock).createUser(form);
    }

    @MethodSource("emptyStrings")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenNameIsEmpty(String emptyName) throws Exception {
        var formWithEmptyName = new CreateUserForm(emptyName,form.getPassword(),form.getRepeatedPassword(),form.getEmail());

        prepareCreateForm(formWithEmptyName)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","name"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("emptyStrings")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenEmailIsEmpty(String emptyEmail) throws Exception {
        var formWithEmptyEmail = new CreateUserForm(form.getName(),form.getPassword(),form.getRepeatedPassword(),emptyEmail);
        prepareCreateForm(formWithEmptyEmail)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","email"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("invalidEmails")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenEmailIsInvalid(String invalidEmail) throws Exception {
        var formWithInvalidEmail = new CreateUserForm(form.getName(),form.getPassword(),form.getRepeatedPassword(),invalidEmail);
        prepareCreateForm(formWithInvalidEmail)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","email"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("emptyStrings")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenPasswordIsEmpty(String emptyPassword) throws Exception {
        var formWithEmptyPassword = new CreateUserForm(form.getName(),emptyPassword,emptyPassword,form.getEmail());
        prepareCreateForm(formWithEmptyPassword)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","password"));

        verifyNoInteractions(userServiceMock);
    }

    @MethodSource("invalidPasswords")
    @ParameterizedTest
    void shouldReturnUserBackWithErrorWhenPasswordIsInvalid(String invalidPassword) throws Exception {
        var formWithInvalidPassword = new CreateUserForm(form.getName(),invalidPassword,invalidPassword,form.getEmail());

        prepareCreateForm(formWithInvalidPassword)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("form","password"));

        verifyNoInteractions(userServiceMock);
    }

    @Test
    void shouldReturnUserBackWithErrorWhenTwoPasswordsDoNotMatch() throws Exception {
        var formWithTwoNonMatchingPasswords = new CreateUserForm(form.getName(),form.getPassword(),form.getPassword()+"Diff",form.getEmail());

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

    private ResultActions prepareCreateForm(CreateUserForm form) throws Exception {
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
