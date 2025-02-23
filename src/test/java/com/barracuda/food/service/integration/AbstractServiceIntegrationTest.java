package com.barracuda.food.service.integration;

import com.barracuda.food.AbstractTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(IntegrationTestConfiguration.class)
@SpringBootTest
public class AbstractServiceIntegrationTest extends AbstractTest {

}
