package com.heera.employee_leave_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EmployeeLeavePortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeLeavePortalApplication.class, args);
	}

}
