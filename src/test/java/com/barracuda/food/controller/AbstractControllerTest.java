package com.barracuda.food.controller;

import com.barracuda.food.AbstractTest;
import com.barracuda.food.configuration.FSecurityConfiguration;
import com.barracuda.food.entity.User;
import com.barracuda.food.service.AdminService;
import com.barracuda.food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@MockitoBean(types = {UserService.class,AdminService.class,OneTimeTokenService.class})
@Import({FSecurityConfiguration.class, ControllerTestConfiguration.class})
@WebMvcTest
public class AbstractControllerTest extends AbstractTest {

    @Autowired
    protected MockMvc mockMvc;

}
