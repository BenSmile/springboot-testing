package com.bkafirongo.springboottesting.intergration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractContainerBaseTest {

     static  MySQLContainer MYSQL_CONTAINER;

     static {
         MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
                 .withPassword("root")
                 .withDatabaseName("employeedb")
                 .withUsername("root");
         MYSQL_CONTAINER.start();
     }

    @DynamicPropertySource
    static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
    }
}
