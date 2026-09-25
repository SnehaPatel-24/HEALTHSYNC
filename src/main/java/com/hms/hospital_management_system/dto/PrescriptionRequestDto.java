package com.hms.hospital_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionRequestDto {

    @NotBlank(message = "Diagnosis is Required")
    private String diagnosis;
    @NotBlank(message = "medicines are Required")
    private String medicines;
    @NotBlank(message = "Dosage is Required")
    private  String dosage;
    @NotBlank(message = "instructions are Required")
    private String instructions;

    @NotNull(message = "Patient Id is required")
    private Long patientId;

    @NotNull(message = "Doctor Id is required")
    private Long doctorId;

    @NotNull(message = "Appointment Id is required")
    private  Long appointmentId;

}
