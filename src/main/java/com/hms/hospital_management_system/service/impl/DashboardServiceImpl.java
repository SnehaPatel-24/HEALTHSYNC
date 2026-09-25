package com.hms.hospital_management_system.service.impl;

import com.hms.hospital_management_system.dto.DashboardStatsDto;
import com.hms.hospital_management_system.enums.AppointmentStatus;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.repository.PrescriptionRepository;
import com.hms.hospital_management_system.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final AppointmentRepository appointmentRepository;

    private final PrescriptionRepository prescriptionRepository;


    @Override
    public DashboardStatsDto getDashboardStats() {

        return DashboardStatsDto.builder()
                .totalPatients(patientRepository.count())
                .totalDoctors(doctorRepository.count())
                .totalAppointments(appointmentRepository.count())
                .totalPrescriptions(prescriptionRepository.count())

                .todayAppointments(
                        appointmentRepository
                                .countByAppointmentDate(
                                        LocalDate.now()))

                .completedAppointments(
                        appointmentRepository
                                .countByStatus(
                                        AppointmentStatus.COMPLETED))

                .cancelledAppointments(
                        appointmentRepository
                                .countByStatus(
                                        AppointmentStatus.CANCELLED))

                .build();
    }
}
