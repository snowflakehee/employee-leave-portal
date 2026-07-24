package com.heera.employee_leave_portal.controller;

import com.heera.employee_leave_portal.entity.Employee;
import com.heera.employee_leave_portal.entity.User;
import com.heera.employee_leave_portal.repository.EmployeeRepository;
import com.heera.employee_leave_portal.repository.UserRepository;
import com.heera.employee_leave_portal.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private EmployeeRepository employeeRepository;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        if (user.getEmployee() != null && user.getEmployee().getId() != null) {
            Employee employee = employeeRepository.findById(user.getEmployee().getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with id " + user.getEmployee().getId()));
            user.setEmployee(employee);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        User user = userRepository.findByUsername(credentials.get("username"))
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(credentials.get("password"), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return Map.of("token", token);
    }

    @GetMapping("/me")
    public Map<String, Object> me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("role", user.getRole());
        result.put("employee", user.getEmployee());
        return result;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PutMapping("/link-employee/{userId}")
    public User linkEmployee(@PathVariable Long userId, @RequestBody Map<String, Long> body) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Employee employee = employeeRepository.findById(body.get("employeeId"))
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        user.setEmployee(employee);
        return userRepository.save(user);
    }
}
