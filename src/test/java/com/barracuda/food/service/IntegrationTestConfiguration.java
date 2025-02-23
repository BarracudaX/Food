package com.barracuda.food.service;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

@Configuration
public class IntegrationTestConfiguration {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mySQLContainer(){
        return new MySQLContainer<>("mysql:latest");
    }

}
