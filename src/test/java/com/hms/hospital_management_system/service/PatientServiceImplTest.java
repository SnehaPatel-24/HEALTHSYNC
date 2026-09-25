package com.hms.hospital_management_system.service;

import com.hms.hospital_management_system.dto.PatientRequestDto;
import com.hms.hospital_management_system.dto.PatientResponseDto;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.exception.ResourceNotFoundException;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void shouldReturnPatientWhenPatientExists() {
        Patient patient = new Patient();

        patient.setId(1L);
        patient.setPatientCode("PAT001");
        patient.setFirstName("Rahul");
        patient.setLastName("Sharma");
        patient.setEmail("rahul@gmail.com");

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));

        PatientResponseDto response =
                patientService.getPatientById(1L);

        assertEquals(1L, response.getId());
        assertEquals("PAT001", response.getPatientCode());
        assertEquals("Rahul", response.getFirstName());

        verify(patientRepository).findById(1L);
    }


    @Test
    void shouldThrowExceptionWhenPatientNotFound() {
        when(patientRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> patientService.getPatientById(1L)
        );
    }


    @Test
    void shouldCreatePatientSuccessfully() {

        // Arrange
        PatientRequestDto requestDto = new PatientRequestDto();
        requestDto.setFirstName("Rahul");
        requestDto.setLastName("Sharma");
        requestDto.setEmail("rahul@gmail.com");
        requestDto.setMobileNumber("9876543210");

        Patient savedPatient = new Patient();
        savedPatient.setId(1L);
        savedPatient.setPatientCode("PAT001");
        savedPatient.setFirstName("Rahul");
        savedPatient.setLastName("Sharma");
        savedPatient.setEmail("rahul@gmail.com");
        savedPatient.setMobileNumber("9876543210");

        when(patientRepository.save(any(Patient.class)))
                .thenReturn(savedPatient);

        // Act
        PatientResponseDto response = patientService.createPatient(requestDto);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals("PAT001", response.getPatientCode());
        assertEquals("Rahul", response.getFirstName());
        assertEquals("Sharma", response.getLastName());
        assertEquals("rahul@gmail.com", response.getEmail());
        assertEquals("9876543210", response.getMobileNumber());

        verify(patientRepository).save(any(Patient.class));
    }


    @Test
    void shouldUpdatePatientSuccessfully(){

        // Arrange
        PatientRequestDto requestDto = new PatientRequestDto();
        requestDto.setFirstName("Rahul");
        requestDto.setLastName("Sharma");
        requestDto.setEmail("rahul@gmail.com");
        requestDto.setMobileNumber("9876543210");

        Patient existingPatient = new Patient();
        existingPatient.setId(1L);
        existingPatient.setPatientCode("PAT001");
        existingPatient.setFirstName("Old Name");
        existingPatient.setLastName("Old Last");
        existingPatient.setEmail("old@gmail.com");
        existingPatient.setMobileNumber("1111111111");

        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setPatientCode("PAT001");
        updatedPatient.setFirstName("Rahul");
        updatedPatient.setLastName("Sharma");
        updatedPatient.setEmail("rahul@gmail.com");
        updatedPatient.setMobileNumber("9876543210");


        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(existingPatient));

        when(patientRepository.save(any(Patient.class)))
                .thenReturn(updatedPatient);

        // Act
        PatientResponseDto response =
                patientService.updatePatient(1L, requestDto);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals("PAT001", response.getPatientCode());
        assertEquals("Rahul", response.getFirstName());
        assertEquals("Sharma", response.getLastName());
        assertEquals("rahul@gmail.com", response.getEmail());

        verify(patientRepository).findById(1L);
        verify(patientRepository).save(any(Patient.class));
    }


    @Test
    void shouldThrowExceptionWhenUpdatingPatientNotFound() {

        when(patientRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> patientService.updatePatient(1L, new PatientRequestDto())
        );

        verify(patientRepository).findById(1L);
    }

    @Test
    void shouldDeletePatientSuccessfully() {

        // Act
        patientService.deletePatient(1L);

        // Assert
        verify(patientRepository).deleteById(1L);
    }

    @Test
    void shouldSearchPatientsSuccessfully() {

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setPatientCode("PAT001");
        patient.setFirstName("Rahul");
        patient.setLastName("Sharma");
        patient.setDisease("Fever");

        when(patientRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDiseaseContainingIgnoreCase(
                        "Rah",
                        "Rah",
                        "Rah"))
                .thenReturn(List.of(patient));

        List<PatientResponseDto> response =
                patientService.searchPatients("Rah");

        assertEquals(1, response.size());
        assertEquals("Rahul", response.get(0).getFirstName());

        verify(patientRepository)
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDiseaseContainingIgnoreCase(
                        "Rah",
                        "Rah",
                        "Rah");
    }

    @Test
    void shouldReturnEmptyListWhenNoPatientFound() {

        when(patientRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDiseaseContainingIgnoreCase(
                        "XYZ",
                        "XYZ",
                        "XYZ"))
                .thenReturn(Collections.emptyList());

        List<PatientResponseDto> response =
                patientService.searchPatients("XYZ");

        assertTrue(response.isEmpty());

        verify(patientRepository)
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDiseaseContainingIgnoreCase(
                        "XYZ",
                        "XYZ",
                        "XYZ");
    }
}