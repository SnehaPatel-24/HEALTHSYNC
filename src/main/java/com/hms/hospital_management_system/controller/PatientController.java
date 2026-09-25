package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.dto.PatientRequestDto;
import com.hms.hospital_management_system.dto.PatientResponseDto;
import com.hms.hospital_management_system.exception.InvalidSortFieldException;
import com.hms.hospital_management_system.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management APIs", description = "APIs for managing hospital patients")
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Create a new patient — returns 201 CREATED")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<PatientResponseDto> createPatient(
            @Valid @RequestBody PatientRequestDto requestDto) {

        PatientResponseDto patient = patientService.createPatient(requestDto);
        return new ResponseEntity<>(patient, HttpStatus.CREATED); // 201
    }

    @Operation(summary = "Get all patients with pagination — returns 200 OK")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<Page<PatientResponseDto>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        List<String> allowedFields = List.of("id", "firstName", "lastName", "createdAt");

        if (!allowedFields.contains(sortBy)) {
            throw new InvalidSortFieldException("Invalid sort field");
        }

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return new ResponseEntity<>(patientService.getAllPatients(pageable), HttpStatus.OK); // 200
    }

    @Operation(summary = "Get patient by ID — returns 200 OK or 404 NOT FOUND")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Long id) {

        return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK); // 200
    }

    @Operation(summary = "Update patient details — returns 200 OK")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequestDto patientRequestDto) {

        return new ResponseEntity<>(
                patientService.updatePatient(id, patientRequestDto), HttpStatus.OK); // 200
    }

    @Operation(summary = "Delete patient — returns 200 OK")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return new ResponseEntity<>("Patient deleted successfully", HttpStatus.OK); // 200
    }

    @Operation(summary = "Search patients by keyword — returns 200 OK")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<List<PatientResponseDto>> searchPatients(
            @RequestParam String keyword) {

        return new ResponseEntity<>(
                patientService.searchPatients(keyword), HttpStatus.OK); // 200
    }
}
