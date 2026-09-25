package com.hms.hospital_management_system.service;

import com.hms.hospital_management_system.dto.PrescriptionRequestDto;
import com.hms.hospital_management_system.dto.PrescriptionResponseDto;

import java.util.List;

public interface PrescriptionService {

    PrescriptionResponseDto createPrescription(
            PrescriptionRequestDto requestDto);

    List<PrescriptionResponseDto> getAllPrescription();

    PrescriptionResponseDto getPrescriptionById(Long id);

    PrescriptionResponseDto updatePrescription(Long id,PrescriptionRequestDto requestDto);

    void deletePrescription(Long id);

    List<PrescriptionResponseDto> getPrescriptionsByPatientId(Long patientId);

    List<PrescriptionResponseDto> getPrescriptionsByDoctorId(Long doctorId);


}
