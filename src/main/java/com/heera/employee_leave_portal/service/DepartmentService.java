package com.heera.employee_leave_portal.service;

import com.heera.employee_leave_portal.entity.Department;
import com.heera.employee_leave_portal.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Cacheable(value = "departments", key = "#id")
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id " + id));
    }

    @CacheEvict(value = "departments", key = "#id")
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
