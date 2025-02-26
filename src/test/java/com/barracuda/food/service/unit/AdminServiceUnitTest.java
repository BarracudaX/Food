package com.barracuda.food.service.unit;

import com.barracuda.food.dto.CreateUserForm;
import com.barracuda.food.entity.Owner;
import com.barracuda.food.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see com.barracuda.food.service.AdminService
 */
public class AdminServiceUnitTest extends AbstractServiceUnitTest{

    private final CreateUserForm form = new CreateUserForm("SOME_NAME","SOME_PASS","SOME_PASS","SOME_EMAIL");
    private final Owner owner = new Owner(form.getName(),form.getEmail(),"ENCODED_PASSWORD");
    private final AdminService adminService;

    public AdminServiceUnitTest(AdminService adminService) {
        this.adminService = adminService;
    }

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(form.getPassword())).thenReturn(owner.getPassword());
        when(userRepositoryMock.save(owner)).thenReturn(owner);
    }

    @Test
    void shouldCreateNewOwner() {
            var savedOwner = ArgumentCaptor.forClass(Owner.class);
            adminService.createOwner(form);

            verify(passwordEncoder).encode(form.getPassword());
            verify(userRepositoryMock).save(savedOwner.capture());
            assertThat(savedOwner.getValue()).usingRecursiveComparison().isEqualTo(owner);
    }

}
