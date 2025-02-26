package com.barracuda.food.service.integration;

import com.barracuda.food.dto.CreateUserForm;
import com.barracuda.food.entity.enums.Role;
import com.barracuda.food.repository.UserRepository;
import com.barracuda.food.service.AdminService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AdminServiceIntegrationTest extends AbstractServiceIntegrationTest{

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final CreateUserForm form = new CreateUserForm("SOME_NAME","SomePass123!","SomePass123!","some@email.com");

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateNewOwner() {
        SoftAssertions assertions = new SoftAssertions();
        assertThat(userRepository.findByEmail(form.getEmail())).isEmpty();

        adminService.createOwner(form);
        var owner = userRepository.findByEmail(form.getEmail()).get();

        assertions.assertThat(owner.getName()).isEqualTo(form.getName());
        assertions.assertThat(owner.getEmail()).isEqualTo(form.getEmail());
        assertions.assertThat(owner.getPassword()).satisfies(password -> passwordEncoder.matches(form.getPassword(),password));
        assertions.assertThat(owner.getAuthorities()).hasSize(1);
        assertions.assertThat(owner.getAuthorities().iterator().next()).isEqualTo(new SimpleGrantedAuthority("ROLE_"+ Role.OWNER.name()));

        assertions.assertAll();
    }

    @Test
    void shouldThrowDataIntegrityViolationConstrainWhenCreatingOwnerWithDuplicateEmail() {
        adminService.createOwner(form);

        assertThatThrownBy(() -> adminService.createOwner(form)).isInstanceOf(DataIntegrityViolationException.class).hasMessageContaining("email");
    }
}
