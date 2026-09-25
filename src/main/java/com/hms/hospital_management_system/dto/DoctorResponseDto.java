package com.hms.hospital_management_system.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
public class DoctorResponseDto {

    private  Long id;

    private String doctorCode;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String specialization;

    private Integer experienceYears;

    private LocalDateTime createdAt;


    private  LocalDateTime updatedAt;
}
