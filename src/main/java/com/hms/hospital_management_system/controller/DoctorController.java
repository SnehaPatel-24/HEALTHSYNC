package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.dto.DoctorRequestDto;
import com.hms.hospital_management_system.dto.DoctorResponseDto;
import com.hms.hospital_management_system.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(
        name = "Doctor Management APIs",
        description = "APIs for managing hospital doctors"
)
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Create a new Doctor — returns 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DoctorResponseDto> createDoctor(
            @Valid @RequestBody DoctorRequestDto requestDto) {

        DoctorResponseDto doctor = doctorService.createDoctor(requestDto);
        return new ResponseEntity<>(doctor, HttpStatus.CREATED); // 201
    }

    @Operation(summary = "Get all doctors — returns 200 OK")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    @GetMapping
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors() {

        List<DoctorResponseDto> allDoctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(allDoctors, HttpStatus.OK); // 200
    }

    @Operation(summary = "Get doctor by ID — returns 200 OK or 404 NOT FOUND")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(@PathVariable Long id) {

        DoctorResponseDto doctorById = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctorById); // 200
    }

    @Operation(summary = "Update doctor details — returns 200 OK")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequestDto requestDto) {

        DoctorResponseDto doctorResponseDto = doctorService.updateDoctor(id, requestDto);
        return new ResponseEntity<>(doctorResponseDto, HttpStatus.OK); // 200
    }

    @Operation(summary = "Delete doctor — returns 200 OK")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return new ResponseEntity<>("Doctor deleted successfully", HttpStatus.OK); // 200
    }

    @Operation(summary = "Get doctor by specialization — returns 200 OK")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorResponseDto>> getDoctorBySpecialization(
            @PathVariable String specialization) {

        List<DoctorResponseDto> doctors = doctorService.getDoctorBySpecialization(specialization);
        return new ResponseEntity<>(doctors, HttpStatus.OK); // 200
    }
}
