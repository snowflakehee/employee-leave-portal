package com.heera.employee_leave_portal.repository;

import com.heera.employee_leave_portal.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
