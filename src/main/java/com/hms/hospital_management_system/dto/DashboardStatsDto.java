package com.hms.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDto {

    private long totalPatients;

    private long totalDoctors;

    private long totalAppointments;

    private long totalPrescriptions;

    private long todayAppointments;

    private long completedAppointments;

    private long cancelledAppointments;

}


