package com.barracuda.food.controller;

import com.barracuda.food.AbstractTest;
import com.barracuda.food.configuration.FSecurityConfiguration;
import com.barracuda.food.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

@Import({ControllerTestConfiguration.class, FSecurityConfiguration.class})
@WebMvcTest
public class AbstractControllerTest extends AbstractTest {

    @Autowired
    protected MockMvc mockMvc;

}
