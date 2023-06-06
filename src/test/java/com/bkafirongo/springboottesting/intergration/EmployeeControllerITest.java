package com.bkafirongo.springboottesting.intergration;

import com.bkafirongo.springboottesting.exception.ResourceNotFoundException;
import com.bkafirongo.springboottesting.model.Employee;
import com.bkafirongo.springboottesting.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class EmployeeControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        employee = Employee
                .builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("email@example.com")
                .build();
    }

    @DisplayName("Integration test for create employee")
    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup

        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(post("/api/employees")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("Integration test for get all employees")
    @Test
    void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        // given - precondition or setup
        employeeRepository.saveAll(List.of(employee));
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(get("/api/employees"));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @DisplayName("Integration test for get employee by id  | Positive scenario")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenEmployeeObject() throws Exception {
        // given - precondition or setup
        Employee savedEmployee = employeeRepository.save(employee);
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(get("/api/employees/{id}", savedEmployee.getId()));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("Integration test for get employee by id  | Negative scenario")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        var employeeId = 1L;
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Integration test for update employee | Positive scenario")
    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenEmployeeObject() throws Exception {
        // given - precondition or setup
        var savedEmployee = employeeRepository.save(employee);

        var updatedEmployee = Employee
                .builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .build();
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .content(objectMapper.writeValueAsString(updatedEmployee))
                .contentType(APPLICATION_JSON));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("Integration test for update employee | Negative scenario")
    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        var employeeId = 1L;
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .content(objectMapper.writeValueAsString(employee))
                .contentType(APPLICATION_JSON));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Integration test for delete employee | Positive scenario")
    @Test
    void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given - precondition or setup
        var savedEmployee = employeeRepository.save(employee);
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Integration test for delete employee | Negative scenario")
    @Test
    void givenEmployeeId_whenDeleteEmployee_thenReturn400() throws Exception {
        // given - precondition or setup
        var employeeId = 1L;
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(delete("/api/employees/{id}", employeeId));
        // then -verify the output
        response.andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }
}
