package com.hms.hospital_management_system.service;

import com.hms.hospital_management_system.dto.DoctorRequestDto;
import com.hms.hospital_management_system.dto.DoctorResponseDto;

import java.util.List;

public interface DoctorService {

    DoctorResponseDto createDoctor(DoctorRequestDto requestDto);

    List<DoctorResponseDto> getAllDoctors();

    DoctorResponseDto getDoctorById(Long id);

    DoctorResponseDto updateDoctor(Long id,DoctorRequestDto requestDto);

    void deleteDoctor(Long id);

    List<DoctorResponseDto> getDoctorBySpecialization(String specialization);

}
