package com.bkafirongo.springboottesting.repository;

import com.bkafirongo.springboottesting.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

//    Junit for save employee operation

    /**
     * @bdd covention give_when_then()
     * @given - precondition or setup
     * @when - action or condition to test
     * @then - verify the output
     */

    @DisplayName("Junit test for save employee operation")
    @Test
    void giveEmployeeObject_whenSave_thenReturnSavedEmployee() {
        Employee employee = Employee
                .builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@example.com")
                .build();
        Employee savedEmployee = employeeRepository.save(employee);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isPositive();
    }


}
