    package com.hms.hospital_management_system.entity;

    import com.hms.hospital_management_system.enums.AppointmentStatus;
    import jakarta.persistence.*;
    import lombok.Data;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.LocalTime;

    @Entity
    @Data
    public class Appointment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String appointmentCode;

        private Long patientId;

        private String doctorName;

        private LocalDate appointmentDate;

        private LocalTime appointmentTime;

        @Enumerated(EnumType.STRING)
        private AppointmentStatus status;

        private String remarks;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;




    }
