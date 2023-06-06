package com.bkafirongo.springboottesting.repository;

import com.bkafirongo.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        employee = Employee
                .builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@example.com")
                .build();
    }

    /**
     * @bdd convention give_when_then()
     * @given - precondition or setup
     * @when - action or condition to test
     * @then - verify the output
     */

    @DisplayName("JUnit test for save employee operation")
    @Test
    void giveEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //when
        Employee savedEmployee = employeeRepository.save(employee);
        //then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isPositive();
    }

    @DisplayName("JUnit test for get all employees list")
    @Test
    void givenEmployeeList_whenFindAll_thenReturnEmployeeList() {
        Employee employee2 = getEmployee("jane@employee.com", "Jane", "Doe");
        employeeRepository.save(employee);
        employeeRepository.save(employee2);
        // when - condition or the behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();
        // then - verify the output
        assertThat(employeeList)
                .isNotNull()
                .hasSize(2);
    }

    @DisplayName("JUnit test for get employee by id operation")
    @Test
    void givenEmployeeObject_whenFindByID_thenReturnEmployeeFromDB() {
        // given - precondition or setup
        employee = employeeRepository.save(employee);
        // when - condition or the behaviour that we are going to test
        Optional<Employee> employeeInDB = employeeRepository.findById(employee.getId());
        // then -verify
        assertThat(employeeInDB).isPresent();
        assertThat(employeeInDB.get().getId()).isPositive();
    }


    @DisplayName("JUnit test for get employee by email operation")
    @Test
    void givenEmployeeEmail_whenFindByEmail_thenEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - condition or the behaviour that we are going to test
        Optional<Employee> employeeInDB = employeeRepository.findByEmail("email@example.com");
        // then -verify
        assertThat(employeeInDB).isPresent();
    }

    @DisplayName("JUnit test for udpate employee operation")
    @Test
    void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - condition or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("new-email@example.com");
        savedEmployee.setFirstName("ben");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);
        // then -verify
        assertThat(updatedEmployee.getEmail()).isEqualTo("new-email@example.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("ben");
    }

    // JUnit test for
    @DisplayName("JUnit test for delete employee operation")
    @Test
    void givenEmployeeObject_whenDelete_thenRemoveEmployeeFromDB() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - condition or the behaviour that we are going to test
        employeeRepository.delete(employee);
        // then -verify the output
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());
        assertThat(optionalEmployee).isNotPresent();
    }

    @DisplayName("JUnit test for customer query using JPQL with index param")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - condition or the behaviour that we are going to test
        String firstName = "John";
        String lastName = "Doe";
        Employee savedEmp = employeeRepository.findByJPQL(firstName, lastName);
        // then -verify the output
        assertThat(savedEmp).isNotNull();
    }

    @DisplayName("JUnit test for customer query using JPQL with named params")
    @Test
    void givenFirstNameAndLastName_whenFindByFirstNameAndLastName_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - condition or the behaviour that we are going to test
        String firstName = "John";
        String lastName = "Doe";
        Employee savedEmp = employeeRepository.findByJPQLNamedParam(firstName, lastName);
        // then -verify the output
        assertThat(savedEmp).isNotNull();
    }

    @DisplayName("JUnit test for custom query using native query")
    @Test
    void givenFirstNameAndLastName_whenNativeQuery_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - condition or the behaviour that we are going to test
        String firstName = "John";
        String lastName = "Doe";
        Employee savedEmp = employeeRepository.findByNativeSQLParam(firstName, lastName);
        // then -verify the output
        assertThat(savedEmp).isNotNull();
    }

    @DisplayName("JUnit test for custom query using native query with named params")
    @Test
    void givenFirstNameAndLastName_whenNativeQueryNamedParam_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - condition or the behaviour that we are going to test
        String firstName = "John";
        String lastName = "Doe";
        Employee savedEmp = employeeRepository.findByNativeSQLNamed(firstName, lastName);
        // then -verify the output
        assertThat(savedEmp).isNotNull();
    }

    Employee getEmployee(String email, String firstname, String lastName) {
        return Employee
                .builder()
                .firstName(firstname)
                .lastName(lastName)
                .email(email)
                .build();
    }

}
