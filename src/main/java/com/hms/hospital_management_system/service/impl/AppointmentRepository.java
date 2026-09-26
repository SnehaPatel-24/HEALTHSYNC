package com.hms.hospital_management_system.repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    long countByAppointmentDate(LocalDate appointmentDate);

    long countByStatus(AppointmentStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Appointment a WHERE a.doctorName = :doctorName AND a.appointmentDate = :appointmentDate AND a.appointmentTime = :appointmentTime AND a.status = :status")
    Optional<Appointment> findConflictingAppointmentForUpdate(
            @Param("doctorName") String doctorName,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("appointmentTime") LocalTime appointmentTime,
            @Param("status") AppointmentStatus status
    );

    List<Appointment> findByAppointmentDateAndAppointmentTimeAndStatus(
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            AppointmentStatus status
    );
}