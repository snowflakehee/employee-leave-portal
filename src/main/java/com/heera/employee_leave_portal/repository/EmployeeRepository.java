package com.heera.employee_leave_portal.repository;

import com.heera.employee_leave_portal.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}