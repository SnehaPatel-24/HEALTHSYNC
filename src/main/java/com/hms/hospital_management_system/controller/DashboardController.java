package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.dto.DashboardStatsDto;
import com.hms.hospital_management_system.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(
        name = "Dashboard APIs",
        description = "Hospital dashboard statistics and analytics"
)
public class DashboardController {

private final DashboardService dashboardService;

    @Operation(
            summary = "Get Dashboard Statistics",
            description = "Retrieves dashboard statistics including total patients, doctors, appointments, and other system metrics. Accessible only by ADMIN users."
    )
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
@GetMapping("/stats")
public ResponseEntity<DashboardStatsDto> getDashboardStats(){

    return ResponseEntity.ok(dashboardService.getDashboardStats());
}


}
