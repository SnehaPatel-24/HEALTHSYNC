package com.hms.hospital_management_system.repository;

import com.hms.hospital_management_system.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {

    List<Doctor> findBySpecialization(String specialization);

    boolean existsByEmail(String email);
}
