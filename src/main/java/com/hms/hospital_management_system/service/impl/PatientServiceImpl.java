package com.hms.hospital_management_system.service.impl;

import com.hms.hospital_management_system.dto.PatientRequestDto;
import com.hms.hospital_management_system.dto.PatientResponseDto;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.exception.ResourceNotFoundException;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {


    private final PatientRepository patientRepository;

    @Override
    public PatientResponseDto createPatient(PatientRequestDto requestDto) {

        Patient patient = Patient.builder()
                .patientCode("PAT" + System.currentTimeMillis())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .gender(requestDto.getGender())
                .dateOfBirth(requestDto.getDateOfBirth())
                .mobileNumber(requestDto.getMobileNumber())
                .email(requestDto.getEmail())
                .address(requestDto.getAddress())
                .bloodGroup(requestDto.getBloodGroup())
                .disease(requestDto.getDisease())
                .allergies(requestDto.getAllergies())
                .build();

        Patient savedPatient = patientRepository.save(patient);

        PatientResponseDto responseDto = new PatientResponseDto();

        responseDto.setId(savedPatient.getId());
        responseDto.setPatientCode(savedPatient.getPatientCode());
        responseDto.setFirstName(savedPatient.getFirstName());
        responseDto.setLastName(savedPatient.getLastName());
        responseDto.setGender(savedPatient.getGender());
        responseDto.setDateOfBirth(savedPatient.getDateOfBirth());
        responseDto.setMobileNumber(savedPatient.getMobileNumber());
        responseDto.setEmail(savedPatient.getEmail());
        responseDto.setAddress(savedPatient.getAddress());
        responseDto.setBloodGroup(savedPatient.getBloodGroup());
        responseDto.setDisease(savedPatient.getDisease());
        responseDto.setAllergies(savedPatient.getAllergies());
        responseDto.setCreatedAt(savedPatient.getCreatedAt());

        return responseDto;

    }

    @Override
    public Page<PatientResponseDto> getAllPatients(Pageable pageable) {

        Page<Patient> patients = patientRepository.findAll(pageable);

        return patients.map(patient -> PatientResponseDto.builder()
                .id(patient.getId())
                .patientCode(patient.getPatientCode())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .gender(patient.getGender())
                .dateOfBirth(patient.getDateOfBirth())
                .mobileNumber(patient.getMobileNumber())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .bloodGroup(patient.getBloodGroup())
                .disease(patient.getDisease())
                .allergies(patient.getAllergies())
                .createdAt(patient.getCreatedAt())
                .build());
    }

    @Override
    public PatientResponseDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient Not Found"));


        PatientResponseDto responseDto = new PatientResponseDto();

        responseDto.setId(patient.getId());
        responseDto.setPatientCode(patient.getPatientCode());
        responseDto.setFirstName(patient.getFirstName());
        responseDto.setLastName(patient.getLastName());
        responseDto.setGender(patient.getGender());
        responseDto.setDateOfBirth(patient.getDateOfBirth());
        responseDto.setMobileNumber(patient.getMobileNumber());
        responseDto.setEmail(patient.getEmail());
        responseDto.setAddress(patient.getAddress());
        responseDto.setBloodGroup(patient.getBloodGroup());
        responseDto.setDisease(patient.getDisease());
        responseDto.setAllergies(patient.getAllergies());
        responseDto.setCreatedAt(patient.getCreatedAt());

        return responseDto;


    }

    @Override
    public PatientResponseDto updatePatient(Long id, PatientRequestDto requestDto) {


        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient Not Found"));

        existingPatient.setFirstName(requestDto.getFirstName());
        existingPatient.setLastName(requestDto.getLastName());
        existingPatient.setGender(requestDto.getGender());
        existingPatient.setDateOfBirth(requestDto.getDateOfBirth());
        existingPatient.setMobileNumber(requestDto.getMobileNumber());
        existingPatient.setEmail(requestDto.getEmail());
        existingPatient.setAddress(requestDto.getAddress());
        existingPatient.setBloodGroup(requestDto.getBloodGroup());
        existingPatient.setDisease(requestDto.getDisease());
        existingPatient.setAllergies(requestDto.getAllergies());


        Patient updatedPatient = patientRepository.save(existingPatient);

        PatientResponseDto responseDto = new PatientResponseDto();

        responseDto.setId(updatedPatient.getId());
        responseDto.setPatientCode(updatedPatient.getPatientCode());
        responseDto.setFirstName(updatedPatient.getFirstName());
        responseDto.setLastName(updatedPatient.getLastName());
        responseDto.setGender(updatedPatient.getGender());
        responseDto.setDateOfBirth(updatedPatient.getDateOfBirth());
        responseDto.setMobileNumber(updatedPatient.getMobileNumber());
        responseDto.setEmail(updatedPatient.getEmail());
        responseDto.setAddress(updatedPatient.getAddress());
        responseDto.setBloodGroup(updatedPatient.getBloodGroup());
        responseDto.setDisease(updatedPatient.getDisease());
        responseDto.setAllergies(updatedPatient.getAllergies());
        responseDto.setCreatedAt(updatedPatient.getCreatedAt());

        return responseDto;
    }

    @Override
    public void deletePatient(Long id) {
     patientRepository.deleteById(id);
    }




    @Override
    public List<PatientResponseDto> searchPatients(String keyword) {

        List<Patient> patients =
                patientRepository
                        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDiseaseContainingIgnoreCase(
                                keyword,
                                keyword,
                                keyword
                        );

        return patients.stream()
                .map(patient -> PatientResponseDto.builder()
                        .id(patient.getId())
                        .patientCode(patient.getPatientCode())
                        .firstName(patient.getFirstName())
                        .lastName(patient.getLastName())
                        .gender(patient.getGender())
                        .dateOfBirth(patient.getDateOfBirth())
                        .mobileNumber(patient.getMobileNumber())
                        .email(patient.getEmail())
                        .address(patient.getAddress())
                        .bloodGroup(patient.getBloodGroup())
                        .disease(patient.getDisease())
                        .allergies(patient.getAllergies())
                        .createdAt(patient.getCreatedAt())
                        .build())
                .toList();
    }
}
