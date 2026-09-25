package com.hms.hospital_management_system.repository;

import com.hms.hospital_management_system.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {


    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDiseaseContainingIgnoreCase(
            String firstName,
            String lastName,
            String disease
    );
}
