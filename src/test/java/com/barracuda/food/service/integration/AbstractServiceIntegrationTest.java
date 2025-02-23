package com.barracuda.food.service.integration;

import com.barracuda.food.AbstractTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

@Import(IntegrationTestConfiguration.class)
@SpringBootTest
public class AbstractServiceIntegrationTest extends AbstractTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected void cleanDB(){
        JdbcTestUtils.deleteFromTables(
                jdbcTemplate,
                "ingredient_options","ingredients","one_time_tokens","menu_items","menus","branches",
                "restaurants","restaurant_owners","staff","managers","admins","users"
        );
    }

}
