package com.hms.hospital_management_system.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
public class PrescriptionResponseDto {

    private Long id;

    private String prescriptionCode;

    private Long patientId;

    private String patientName;

    private Long doctorId;

    private String doctorName;

    private Long appointmentId;

    private String appointmentCode;

    private String diagnosis;

    private String medicines;

    private  String dosage;

    private String instructions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
