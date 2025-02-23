package com.barracuda.food.service.integration;

import com.barracuda.food.dto.UpdateNameForm;
import com.barracuda.food.dto.UserRegistrationForm;
import com.barracuda.food.entity.enums.Role;
import com.barracuda.food.repository.UserRepository;
import com.barracuda.food.service.UserService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserServiceIntegrationTest extends AbstractServiceIntegrationTest{

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityManager entityManager;

    private final UserRegistrationForm createForm = new UserRegistrationForm("SomeName","some@email.com","SomePass123!","SomePass123!");
    private Pageable pageable = Pageable.ofSize(2);

    @BeforeEach
    void setUp() {
        cleanDB();
    }

    @Test
    void shouldCreateNewUser() {
        var softAssertions = new SoftAssertions();
        assertThat(userRepository.findByEmail(createForm.email())).isEmpty();

        userService.createUser(createForm);
        var user = userRepository.findByEmail(createForm.email()).get();

        softAssertions.assertThat(user.getName()).isEqualTo(createForm.name());
        softAssertions.assertThat(user.getEmail()).isEqualTo(createForm.email());
        softAssertions.assertThat(user.getUsername()).isEqualTo(createForm.email());
        softAssertions.assertThat(user.getId()).isNotNull();
        softAssertions.assertThat(user.getPassword()).satisfies(password -> passwordEncoder.matches(createForm.password(),password));
        softAssertions.assertThat(user.getAuthorities()).hasSize(1);
        softAssertions.assertThat(user.getAuthorities().iterator().next()).isEqualTo(new SimpleGrantedAuthority("ROLE_"+ Role.USER.name()));

        softAssertions.assertAll();
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenCreatingUserWithDuplicateEmail() {
        userService.createUser(createForm);

        assertThatThrownBy(() -> userService.createUser(createForm)).isInstanceOf(DataIntegrityViolationException.class).hasMessageContaining("email");
    }

    @Test
    void shouldReturnEmptyUsersPageWhenThereAreNoUsers() {
        assertThat(userService.users(pageable)).isEmpty();
    }

    @Test
    void shouldReturnPageThatContainsUser() {
        var user = userService.createUser(createForm);

        assertThat(userService.users(pageable)).containsExactly(user);
    }

    @Test
    void shouldThrowEmptyResultDataAccessExceptionWhenTryingToChangeUserNameOfNonExistingUser() {
        var form = new UpdateNameForm("NEW_NAME",10L);

        assertThatThrownBy(() -> userService.changeUserName(form)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldChangeUserName() {
        var user = userService.createUser(createForm);
        var form = new UpdateNameForm("NEW_NAME",user.getId());

        userService.changeUserName(form);

        assertThat(userRepository.findById(user.getId()).get().getName()).isEqualTo(form.getName()).isNotEqualTo(user.getName());
    }
}
