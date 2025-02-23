package com.barracuda.food.service;

import com.barracuda.food.AbstractTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Import(IntegrationTestConfiguration.class)
@SpringBootTest
public class AbstractServiceIntegrationTest extends AbstractTest {

}
