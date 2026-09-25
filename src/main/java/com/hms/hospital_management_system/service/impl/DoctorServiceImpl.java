package com.hms.hospital_management_system.service.impl;

import com.hms.hospital_management_system.dto.DoctorRequestDto;
import com.hms.hospital_management_system.dto.DoctorResponseDto;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.exception.DuplicateEmailException;
import com.hms.hospital_management_system.exception.ResourceNotFoundException;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public DoctorResponseDto createDoctor(DoctorRequestDto requestDto) {

        if (doctorRepository.existsByEmail(
                requestDto.getEmail())) {

            throw new DuplicateEmailException(
                    "Doctor email already exists"
            );
        }

      Doctor doctor=new Doctor();
        doctor.setDoctorCode(
                "DOC" + System.currentTimeMillis()
        );

        doctor.setFullName(requestDto.getFullName());
        doctor.setEmail(requestDto.getEmail());
        doctor.setPhoneNumber(requestDto.getPhoneNumber());
        doctor.setSpecialization(requestDto.getSpecialization());
        doctor.setExperienceYears(requestDto.getExperienceYears());



        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapToResponseDto(savedDoctor) ;
    }

    @Override
    public List<DoctorResponseDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorResponseDto> collect = doctors.stream().map((doctor) -> mapToResponseDto(doctor)).collect(Collectors.toList());

        return collect;
    }

    @Override
    public DoctorResponseDto getDoctorById(Long id) {

        Doctor doctor = doctorRepository.
                findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found with this id : " + id));
        return  mapToResponseDto(doctor);
    }

    @Override
    public DoctorResponseDto updateDoctor(Long id, DoctorRequestDto requestDto) {
        Doctor doctor = doctorRepository.
                findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found with this id : " + id));

        doctor.setFullName(requestDto.getFullName());
        doctor.setEmail(requestDto.getEmail());
        doctor.setPhoneNumber(requestDto.getPhoneNumber());
        doctor.setSpecialization(requestDto.getSpecialization());
        doctor.setExperienceYears(requestDto.getExperienceYears());

        Doctor savedDoctor = doctorRepository.save(doctor);


        return mapToResponseDto(savedDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.
                findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found with this id : " + id));

        doctorRepository.deleteById(id);

    }

    @Override
    public List<DoctorResponseDto> getDoctorBySpecialization(String specialization) {
        List<Doctor> doctors = doctorRepository.findBySpecialization(specialization);
        List<DoctorResponseDto> collect = doctors.stream()
                .map((doctor -> mapToResponseDto(doctor)))
                .collect(Collectors.toList());

        return collect;
    }


    private DoctorResponseDto mapToResponseDto(
            Doctor doctor) {

        return DoctorResponseDto.builder()
                .id(doctor.getId())
                .doctorCode(doctor.getDoctorCode())
                .fullName(doctor.getFullName())
                .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .specialization(doctor.getSpecialization())
                .experienceYears(
                        doctor.getExperienceYears()
                )
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }
}
