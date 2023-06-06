package com.bkafirongo.springboottesting.service.impl;

import com.bkafirongo.springboottesting.exception.ResourceNotFoundException;
import com.bkafirongo.springboottesting.model.Employee;
import com.bkafirongo.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee;

    @BeforeEach
    void setup() {
        employee = Employee
                .builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("email@example.com")
                .build();
    }

    @DisplayName("JUnit test for save employee method")
    @Test
    void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee))
                .willReturn(employee);
        // when - condition or the behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for save employee which throws exception")
    @Test
    void givenEmployeeObject_whenSaveEmployee_thenThrowError() {
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
        // when - condition or the behaviour that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> employeeService
                .saveEmployee(employee));
        // then - verify the output
        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    @DisplayName("JUnit test for get all employees")
    @Test
    void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {
        // given - precondition or setup
        given(employeeRepository.findAll())
                .willReturn(List.of(employee));
        // when - condition or the behaviour that we are going to test
        List<Employee> allEmployees = employeeService.getAllEmployees();
        // then - verify the output
        assertThat(allEmployees)
                .isNotEmpty()
                .hasSize(1);
    }

    @DisplayName("JUnit test for get all employees (empty list)")
    @Test
    void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {
        // given - precondition or setup
        given(employeeRepository.findAll())
                .willReturn(List.of());
        // when - condition or the behaviour that we are going to test
        List<Employee> allEmployees = employeeService.getAllEmployees();
        // then - verify the output
        assertThat(allEmployees)
                .isEmpty();
    }

    @DisplayName("JUnit test for get employee by Id")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // given - precondition or setup
        given(employeeRepository.findById(1L))
                .willReturn(Optional.of(employee));
        // when - condition or the behaviour that we are going to test
        Optional<Employee> optionalEmployee = employeeService
                .getEmployeeById(employee.getId());
        // then - verify the output
        assertThat(optionalEmployee).isPresent();
    }

    @DisplayName("JUnit test for update employee")
    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedUser() {
        // given - precondition or setup
        given(employeeRepository.findById(1L))
                .willReturn(Optional.of(employee));
        given(employeeRepository.save(employee))
                .willReturn(employee);
        employee.setEmail("newemail@email.com");
        employee.setFirstName("Smile");
        // when - condition or the behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(this.employee);
        // then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Smile");
    }

    @DisplayName("JUnit test for update employee which throws exception")
    @Test
    void givenEmployee_whenUpdateEmployeeWhenNotFound_thenThrowException() {
        // given - precondition or setup
        given(employeeRepository.findById(employee.getId()))
                .willReturn(Optional.empty());
        // when - condition or the behaviour that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> employeeService
                .updateEmployee(employee));
        // then - verify the output
        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    @DisplayName("JUnit test for delete employee")
    @Test
    void givenEmployeeId_whenDeleteEmployee_thenDoNothing() {
        // given - precondition or setup
        given(employeeRepository.findById(1L))
                .willReturn(Optional.of(employee));
        willDoNothing()
                .given(employeeRepository)
                .delete(employee);
        // when - condition or the behaviour that we are going to test
        employeeService.deleteEmployee(this.employee.getId());
        // then - verify the output
        verify(employeeRepository, times(1))
                .delete(any(Employee.class));
        verify(employeeRepository, times(1))
                .findById(anyLong());
    }

    @DisplayName("JUnit test for delete employee which throws exception")
    @Test
    void givenEmployee_whenNotExistingEmployee_thenThrowException() {
        // given - precondition or setup
        given(employeeRepository.findById(employee.getId()))
                .willReturn(Optional.empty());
        // when - condition or the behaviour that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> employeeService
                .deleteEmployee(employee.getId()));
        // then - verify the output
        verify(employeeRepository, never())
                .delete(any(Employee.class));
    }
}