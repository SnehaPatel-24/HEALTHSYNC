package com.hms.hospital_management_system.config;

import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Prescription;
import com.hms.hospital_management_system.enums.AppointmentStatus;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    public void run(String... args) throws Exception {
        // If old western seed data is present, update/re-seed with Indian names
        if (doctorRepository.count() == 0 || doctorRepository.findAll().stream().anyMatch(d -> d.getFullName().contains("Sarah Connor"))) {
            prescriptionRepository.deleteAll();
            appointmentRepository.deleteAll();
            patientRepository.deleteAll();
            doctorRepository.deleteAll();
            seedData();
        }
    }

    private void seedData() {
        // 1. Seed Doctors (Indian Names)
        Doctor doc1 = new Doctor();
        doc1.setDoctorCode("DOC101");
        doc1.setFullName("Dr. Ananya Sharma");
        doc1.setEmail("ananya.sharma@healthsync.in");
        doc1.setPhoneNumber("9876543210");
        doc1.setSpecialization("Cardiology");
        doc1.setExperienceYears(12);
        doctorRepository.save(doc1);

        Doctor doc2 = new Doctor();
        doc2.setDoctorCode("DOC102");
        doc2.setFullName("Dr. Rajesh Verma");
        doc2.setEmail("rajesh.verma@healthsync.in");
        doc2.setPhoneNumber("9812345678");
        doc2.setSpecialization("Diagnostic Medicine");
        doc2.setExperienceYears(20);
        doctorRepository.save(doc2);

        Doctor doc3 = new Doctor();
        doc3.setDoctorCode("DOC103");
        doc3.setFullName("Dr. Priya Mehta");
        doc3.setEmail("priya.mehta@healthsync.in");
        doc3.setPhoneNumber("9711223344");
        doc3.setSpecialization("Pediatrics");
        doc3.setExperienceYears(8);
        doctorRepository.save(doc3);

        Doctor doc4 = new Doctor();
        doc4.setDoctorCode("DOC104");
        doc4.setFullName("Dr. Vikram Patel");
        doc4.setEmail("vikram.patel@healthsync.in");
        doc4.setPhoneNumber("9654321098");
        doc4.setSpecialization("General Surgery");
        doc4.setExperienceYears(10);
        doctorRepository.save(doc4);

        Doctor doc5 = new Doctor();
        doc5.setDoctorCode("DOC105");
        doc5.setFullName("Dr. Sunita Gupta");
        doc5.setEmail("sunita.gupta@healthsync.in");
        doc5.setPhoneNumber("9543210987");
        doc5.setSpecialization("Neurosurgery");
        doc5.setExperienceYears(15);
        doctorRepository.save(doc5);

        // 2. Seed Patients (Indian Names)
        Patient pat1 = new Patient();
        pat1.setPatientCode("PAT201");
        pat1.setFirstName("Aarav");
        pat1.setLastName("Sharma");
        pat1.setGender("Male");
        pat1.setDateOfBirth(LocalDate.parse("1992-05-15"));
        pat1.setMobileNumber("9876543210");
        pat1.setEmail("aarav.sharma@gmail.com");
        pat1.setAddress("Sector 62, Noida");
        pat1.setBloodGroup("A+");
        pat1.setDisease("Viral Fever");
        pat1.setAllergies("Dust");
        patientRepository.save(pat1);

        Patient pat2 = new Patient();
        pat2.setPatientCode("PAT202");
        pat2.setFirstName("Sneha");
        pat2.setLastName("Patel");
        pat2.setGender("Female");
        pat2.setDateOfBirth(LocalDate.parse("1995-11-20"));
        pat2.setMobileNumber("8765432109");
        pat2.setEmail("sneha.patel@gmail.com");
        pat2.setAddress("Navrangpura, Ahmedabad");
        pat2.setBloodGroup("O+");
        pat2.setDisease("Hypertension");
        pat2.setAllergies("Pollen");
        patientRepository.save(pat2);

        Patient pat3 = new Patient();
        pat3.setPatientCode("PAT203");
        pat3.setFirstName("Ananya");
        pat3.setLastName("Iyer");
        pat3.setGender("Female");
        pat3.setDateOfBirth(LocalDate.parse("1998-03-08"));
        pat3.setMobileNumber("7654321098");
        pat3.setEmail("ananya.iyer@gmail.com");
        pat3.setAddress("Indiranagar, Bengaluru");
        pat3.setBloodGroup("B+");
        pat3.setDisease("Migraine");
        pat3.setAllergies("None");
        patientRepository.save(pat3);

        Patient pat4 = new Patient();
        pat4.setPatientCode("PAT204");
        pat4.setFirstName("Rohan");
        pat4.setLastName("Kapoor");
        pat4.setGender("Male");
        pat4.setDateOfBirth(LocalDate.parse("1988-04-17"));
        pat4.setMobileNumber("6543210987");
        pat4.setEmail("rohan.kapoor@gmail.com");
        pat4.setAddress("Bandra West, Mumbai");
        pat4.setBloodGroup("AB+");
        pat4.setDisease("Sprained Ankle");
        pat4.setAllergies("Penicillin");
        patientRepository.save(pat4);

        Patient pat5 = new Patient();
        pat5.setPatientCode("PAT205");
        pat5.setFirstName("Vikramaditya");
        pat5.setLastName("Singh");
        pat5.setGender("Male");
        pat5.setDateOfBirth(LocalDate.parse("1982-12-01"));
        pat5.setMobileNumber("5432109876");
        pat5.setEmail("vikram.singh@gmail.com");
        pat5.setAddress("Civil Lines, Jaipur");
        pat5.setBloodGroup("O-");
        pat5.setDisease("Diabetes");
        pat5.setAllergies("Latex");
        patientRepository.save(pat5);

        // 3. Seed Appointments
        Appointment app1 = new Appointment();
        app1.setAppointmentCode("APT301");
        app1.setPatientId(pat1.getId());
        app1.setDoctorName(doc1.getFullName());
        app1.setAppointmentDate(LocalDate.now());
        app1.setAppointmentTime(LocalTime.of(10, 0));
        app1.setStatus(AppointmentStatus.SCHEDULED);
        app1.setRemarks("Routine checkup");
        appointmentRepository.save(app1);

        Appointment app2 = new Appointment();
        app2.setAppointmentCode("APT302");
        app2.setPatientId(pat2.getId());
        app2.setDoctorName(doc2.getFullName());
        app2.setAppointmentDate(LocalDate.now());
        app2.setAppointmentTime(LocalTime.of(11, 30));
        app2.setStatus(AppointmentStatus.COMPLETED);
        app2.setRemarks("Blood pressure consultation");
        appointmentRepository.save(app2);

        Appointment app3 = new Appointment();
        app3.setAppointmentCode("APT303");
        app3.setPatientId(pat3.getId());
        app3.setDoctorName(doc3.getFullName());
        app3.setAppointmentDate(LocalDate.now().plusDays(1));
        app3.setAppointmentTime(LocalTime.of(14, 0));
        app3.setStatus(AppointmentStatus.SCHEDULED);
        app3.setRemarks("Pediatric consultation");
        appointmentRepository.save(app3);

        // 4. Seed Prescription
        Prescription pr1 = new Prescription();
        pr1.setPrescriptionCode("PR401");
        pr1.setDiagnosis("Essential Hypertension");
        pr1.setMedicines("Amlodipine 5mg, Telmisartan 40mg");
        pr1.setDosage("1 tablet daily after breakfast");
        pr1.setInstructions("Reduce salt intake, light daily walking");
        pr1.setPatient(pat2);
        pr1.setDoctor(doc2);
        pr1.setAppointment(app2);
        prescriptionRepository.save(pr1);
    }
}
