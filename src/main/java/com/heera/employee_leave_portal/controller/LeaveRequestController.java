package com.heera.employee_leave_portal.controller;

import com.heera.employee_leave_portal.entity.LeaveRequest;
import com.heera.employee_leave_portal.entity.LeaveStatus;
import com.heera.employee_leave_portal.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping
    public LeaveRequest applyLeave(@RequestBody LeaveRequest request) {
        return leaveRequestService.applyLeave(request);
    }

    @GetMapping("/employee/{employeeId}")
    public List<LeaveRequest> getForEmployee(@PathVariable Long employeeId) {
        return leaveRequestService.getLeaveRequestsForEmployee(employeeId);
    }

    @GetMapping("/pending")
    public List<LeaveRequest> getPending() {
        return leaveRequestService.getPendingRequests();
    }

    @PutMapping("/{id}/review")
    public LeaveRequest review(@PathVariable Long id, @RequestBody Map<String, String> body) {
        LeaveStatus decision = LeaveStatus.valueOf(body.get("decision"));
        String comment = body.get("comment");
        return leaveRequestService.reviewLeave(id, decision, comment);
    }
}