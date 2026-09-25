package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.dto.PrescriptionRequestDto;
import com.hms.hospital_management_system.dto.PrescriptionResponseDto;
import com.hms.hospital_management_system.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Prescription Management APIs",
        description = "APIs for managing patient prescriptions, including creation, retrieval, update, and deletion of prescriptions."
)
@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Operation(
            summary = "Create Prescription",
            description = "Creates a new prescription for a patient by a doctor."
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<PrescriptionResponseDto>
    createPrescription(
            @Valid @RequestBody
            PrescriptionRequestDto requestDto){

        return ResponseEntity.status(
                        HttpStatus.CREATED)
                .body(
                        prescriptionService
                                .createPrescription(
                                        requestDto));
    }

    @Operation(
            summary = "Get All Prescriptions",
            description = "Retrieves all prescriptions available in the system."
    )
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping
    public ResponseEntity<List<PrescriptionResponseDto>> getAllPrescription(){

        return ResponseEntity.ok(prescriptionService.getAllPrescription());

    }

    @Operation(
            summary = "Get Prescription By ID",
            description = "Retrieves a prescription using its unique ID."
    )
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDto> getPrescriptionById(@PathVariable Long id){

        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }


    @Operation(
            summary = "Update Prescription",
            description = "Updates an existing prescription by its ID."
    )
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDto> updatePrescription(
                  @PathVariable Long id,
                  @Valid @RequestBody PrescriptionRequestDto requestDto
                                                  )
    {

        return  ResponseEntity.ok(prescriptionService.updatePrescription(id,requestDto));
    }


    @Operation(
            summary = "Delete Prescription",
            description = "Deletes a prescription from the system by its ID. Only ADMIN users can perform this action."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePrescription(@PathVariable Long id){

        prescriptionService.deletePrescription(id);
        return ResponseEntity.ok("Prescription deleted!!!");
    }

    @Operation(
            summary = "Get Prescriptions By Patient",
            description = "Retrieves all prescriptions associated with a specific patient."
    )
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionResponseDto>>
    getPrescriptionsByPatientId(
            @PathVariable Long patientId){

        return ResponseEntity.ok(
                prescriptionService
                        .getPrescriptionsByPatientId(
                                patientId));
    }

    @Operation(
            summary = "Get Prescriptions By Doctor",
            description = "Retrieves all prescriptions issued by a specific doctor."
    )
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PrescriptionResponseDto>>
    getPrescriptionsByDoctorId(
            @PathVariable Long doctorId){

        return ResponseEntity.ok(
                prescriptionService
                        .getPrescriptionsByDoctorId(
                                doctorId));
    }
}
