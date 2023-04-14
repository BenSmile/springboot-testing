package com.bkafirongo.springboottesting.repository;

import com.bkafirongo.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
