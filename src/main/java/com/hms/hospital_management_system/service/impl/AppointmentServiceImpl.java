package com.hms.hospital_management_system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.hospital_management_system.dto.AppointmentRequestDto;
import com.hms.hospital_management_system.dto.AppointmentResponseDto;
import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.enums.AppointmentStatus;
import com.hms.hospital_management_system.exception.InvalidAppointmentStatusException;
import com.hms.hospital_management_system.exception.ResourceNotFoundException;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.service.AppointmentService;

import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {


    private final AppointmentRepository appointmentRepository;


    @Override
    public AppointmentResponseDto completeAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id : " + id));

        if(appointment.getStatus()!=AppointmentStatus.SCHEDULED){
            throw new InvalidAppointmentStatusException(
                    "Only scheduled appointments can be completed"
            );
        }

         appointment.setStatus(AppointmentStatus.COMPLETED);

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return mapToResponseDto(updatedAppointment);
    }

    @Override
    public AppointmentResponseDto cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id : " + id));

        if(appointment.getStatus()!=AppointmentStatus.SCHEDULED){
            throw new InvalidAppointmentStatusException(
                    "Only scheduled appointments can be cancelled"
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return mapToResponseDto(updatedAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id : " + id
                        ));

        appointmentRepository.delete(appointment);
    }

    @Override
    public AppointmentResponseDto updateAppointment(Long id, AppointmentRequestDto requestDto) {

        Appointment appointment = appointmentRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id : " + id));

          appointment.setPatientId(requestDto.getPatientId());
          appointment.setDoctorName(requestDto.getDoctorName());
          appointment.setAppointmentDate(requestDto.getAppointmentDate());
          appointment.setAppointmentTime(requestDto.getAppointmentTime());
          appointment.setRemarks(requestDto.getRemarks());


        Appointment savedAppointment = appointmentRepository.save(appointment);

      return  mapToResponseDto(savedAppointment);

    }

    @Override
    public AppointmentResponseDto getAppointmentById(Long id) {

        Appointment appointment = appointmentRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id : " + id));
        return  mapToResponseDto(appointment);
    }

    @Override
    public List<AppointmentResponseDto> getAllAppointments() {

        List<Appointment> appointments = appointmentRepository.findAll();

       return appointments.stream().map(this::mapToResponseDto).toList();



    }

    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto requestDto) {
        List<Appointment> sameSlotAppointments = appointmentRepository.findByAppointmentDateAndAppointmentTimeAndStatus(
                requestDto.getAppointmentDate(),
                requestDto.getAppointmentTime(),
                AppointmentStatus.SCHEDULED
        );

        String normalizedNewName = normalizeDoctorName(requestDto.getDoctorName());

        boolean hasConflict = sameSlotAppointments.stream()
                .anyMatch(a -> normalizeDoctorName(a.getDoctorName()).equals(normalizedNewName));

        if (hasConflict) {
            throw new InvalidAppointmentStatusException(
                "Appointment already exists. Please choose another slot."
            );
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentCode("APT" + System.currentTimeMillis());
        appointment.setPatientId(requestDto.getPatientId());
        appointment.setDoctorName(requestDto.getDoctorName());
        appointment.setAppointmentDate(requestDto.getAppointmentDate());
        appointment.setAppointmentTime(requestDto.getAppointmentTime());
        appointment.setRemarks(requestDto.getRemarks());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return mapToResponseDto(savedAppointment);
    }

    private String normalizeDoctorName(String name) {
        if (name == null) return "";
        return name.toLowerCase().replaceAll("[^a-z]", "");
    }


    private AppointmentResponseDto mapToResponseDto(
            Appointment appointment) {

        AppointmentResponseDto dto =
                new AppointmentResponseDto();

        dto.setId(appointment.getId());
        dto.setAppointmentCode(
                appointment.getAppointmentCode()
        );
        dto.setPatientId(
                appointment.getPatientId()
        );
        dto.setDoctorName(
                appointment.getDoctorName()
        );
        dto.setAppointmentDate(
                appointment.getAppointmentDate()
        );
        dto.setAppointmentTime(
                appointment.getAppointmentTime()
        );
        dto.setStatus(
                appointment.getStatus()
        );
        dto.setRemarks(
                appointment.getRemarks()
        );
        dto.setCreatedAt(
                appointment.getCreatedAt()
        );
        dto.setUpdatedAt(
                appointment.getUpdatedAt()
        );

        return dto;
    }
}