package com.heera.employee_leave_portal.repository;

import com.heera.employee_leave_portal.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeId(Long employeeId);
    List<LeaveRequest> findByStatus(com.heera.employee_leave_portal.entity.LeaveStatus status);
}
