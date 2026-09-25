package com.hms.hospital_management_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Doctor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
 private  Long id;

  private String doctorCode;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String specialization;

  private Integer experienceYears;

  @CreationTimestamp
    private LocalDateTime createdAt;

  @UpdateTimestamp
    private  LocalDateTime updatedAt;


}
