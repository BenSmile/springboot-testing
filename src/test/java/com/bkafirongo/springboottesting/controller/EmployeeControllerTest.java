package com.bkafirongo.springboottesting.controller;

import com.bkafirongo.springboottesting.model.Employee;
import com.bkafirongo.springboottesting.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

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

    @DisplayName("JUnit test for get all employees")
    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willReturn(employee);
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

    // JUnit test for
    @Test
    void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        // given - precondition or setup
        given(employeeService.getAllEmployees())
                .willReturn(List.of(employee));
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(get("/api/employees"));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @DisplayName("JUnit test for get employee by id  | Positive scenario")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenEmployeeObject() throws Exception {
        // given - precondition or setup
        var employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(employee));
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("JUnit test for get employee by id  | Negative scenario")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        var employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.empty());
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("JUnit test for update employee | Positive scenario")
    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenEmployeeObject() throws Exception {
        // given - precondition or setup
        var employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willReturn(employee);
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .content(objectMapper.writeValueAsString(employee))
                .contentType(APPLICATION_JSON));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("JUnit test for update employee | Negative scenario")
    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        var employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.empty());
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .content(objectMapper.writeValueAsString(employee))
                .contentType(APPLICATION_JSON));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    // JUnit test for
    @Test
    void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given - precondition or setup
        var employeeId = 1L;
        willDoNothing().given(employeeService)
                .deleteEmployee(employeeId);
        // when - condition or the behaviour that we are going to test
        var response = mockMvc.perform(delete("/api/employees/{id}", employeeId));
        // then -verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }
}