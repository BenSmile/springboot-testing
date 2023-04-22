package com.bkafirongo.springboottesting.service.impl;

import com.bkafirongo.springboottesting.exception.ResourceNotFoundException;
import com.bkafirongo.springboottesting.model.Employee;
import com.bkafirongo.springboottesting.repository.EmployeeRepository;
import com.bkafirongo.springboottesting.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {

        var optionalEmployee = employeeRepository.findByEmail(employee.getEmail());

        if (optionalEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exists with given email address : " + employee.getEmail());
        }

        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        var optionalEmployee = employeeRepository.findById(employee.getId());
        if (optionalEmployee.isEmpty()) {
            throw new ResourceNotFoundException("No User found for Id : " + employee.getId());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @Override
    public void deleteEmployee(long employeeId) {
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("No User found for Id : " + employeeId));
        employeeRepository.delete(employee);
    }
}
