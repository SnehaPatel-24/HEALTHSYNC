package com.hms.hospital_management_system.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hms.hospital_management_system.dto.PatientRequestDto;
import com.hms.hospital_management_system.dto.PatientResponseDto;

import java.util.List;

public interface PatientService {

    PatientResponseDto createPatient(PatientRequestDto requestDto);

    Page<PatientResponseDto> getAllPatients(Pageable pageable);

    PatientResponseDto getPatientById(Long id);

    PatientResponseDto updatePatient(Long id, PatientRequestDto requestDto);

    void deletePatient(Long id);

    List<PatientResponseDto> searchPatients(String keyword);

}
