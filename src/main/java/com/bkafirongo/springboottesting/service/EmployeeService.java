package com.bkafirongo.springboottesting.service;

import com.bkafirongo.springboottesting.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);


    Employee updateEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Optional<Employee> getEmployeeById(Long employeeId);

    void deleteEmployee(long employeeId);

}
