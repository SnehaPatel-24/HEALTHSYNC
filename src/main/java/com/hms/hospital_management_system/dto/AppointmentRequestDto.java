package com.hms.hospital_management_system.dto;

import com.hms.hospital_management_system.enums.AppointmentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.aspectj.bridge.IMessage;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequestDto {

   @NotNull(message = "Patient Id is required")
    private Long patientId;

   @NotBlank(message = "Doctor Name is Required")
    private String doctorName;

   @NotNull (message = "Appointment Date is Required")
    private LocalDate appointmentDate;

   @NotNull(message = "Appointment time is required")
    private LocalTime appointmentTime;

    private String remarks;
}
