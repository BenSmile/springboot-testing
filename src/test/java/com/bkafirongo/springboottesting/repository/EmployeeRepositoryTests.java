package com.bkafirongo.springboottesting.repository;

import com.bkafirongo.springboottesting.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * @bdd covention give_when_then()
     * @given - precondition or setup
     * @when - action or condition to test
     * @then - verify the output
     */

    @DisplayName("Junit test for save employee operation")
    @Test
    void giveEmployeeObject_whenSave_thenReturnSavedEmployee() {

        //give
        Employee employee = Employee
                .builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@example.com")
                .build();
        //when
        Employee savedEmployee = employeeRepository.save(employee);
        //then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isPositive();
    }

    @DisplayName("Junit test for get all employees list")
    @Test
    void givenEmployeeList_whenFindAll_thenReturnEmployeeList() {
        // give - precondition or setup
        Employee employee1 = Employee
                .builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@example.com")
                .build();
        Employee employee2 = Employee
                .builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@example.com")
                .build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        // when - condition or the behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();
        // then - verify the output
        assertThat(employeeList)
                .isNotNull()
                .hasSize(2);
    }


}
