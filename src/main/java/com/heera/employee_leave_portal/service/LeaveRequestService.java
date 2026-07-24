package com.heera.employee_leave_portal.service;

import com.heera.employee_leave_portal.entity.*;
import com.heera.employee_leave_portal.repository.EmployeeRepository;
import com.heera.employee_leave_portal.repository.LeaveRequestRepository;
import com.heera.employee_leave_portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    private Employee getCurrentEmployee() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        if (user.getEmployee() == null) {
            throw new RuntimeException("This account has no linked employee record");
        }

        return user.getEmployee();
    }

    public List<LeaveRequest> getMyLeaves() {
        Employee employee = getCurrentEmployee();
        return leaveRequestRepository.findByEmployeeId(employee.getId());
    }

    public LeaveRequest applyLeave(LeaveRequest request) {
        Employee employee = getCurrentEmployee();

        long daysRequested = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

        if (daysRequested > employee.getLeaveBalance()) {
            throw new IllegalStateException("Insufficient leave balance. Available: " + employee.getLeaveBalance() + ", Requested: " + daysRequested);
        }

        request.setEmployee(employee);
        request.setStatus(LeaveStatus.PENDING);
        return leaveRequestRepository.save(request);
    }

    public List<LeaveRequest> getLeaveRequestsForEmployee(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    public List<LeaveRequest> getPendingRequests() {
        return leaveRequestRepository.findByStatus(LeaveStatus.PENDING);
    }

    public LeaveRequest reviewLeave(Long requestId, LeaveStatus decision, String comment) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        Employee reviewer = getCurrentEmployee();

        if (request.getEmployee().getId().equals(reviewer.getId())) {
            throw new IllegalStateException("You cannot approve or reject your own leave request");
        }

        if (decision == LeaveStatus.APPROVED) {
            Employee employee = request.getEmployee();
            int currentBalance = employee.getLeaveBalance() != null ? employee.getLeaveBalance() : 0;
            long daysTaken = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
            employee.setLeaveBalance(currentBalance - (int) daysTaken);
            employeeRepository.save(employee);
        }

        request.setStatus(decision);
        request.setReviewedBy(reviewer);
        request.setReviewComment(comment);
        return leaveRequestRepository.save(request);
    }
}