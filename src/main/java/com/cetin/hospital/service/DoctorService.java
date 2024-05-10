package com.cetin.hospital.service;

import com.cetin.hospital.model.Doctor;

import com.cetin.hospital.model.Patient;
import com.cetin.hospital.repository.DoctorRepository;
import com.cetin.hospital.repository.PatientRepository;
import com.cetin.hospital.request.DoctorRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final TimeService timeService;
    private final PatientRepository patientRepository;

    public DoctorService(DoctorRepository doctorRepository, TimeService timeService, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.timeService = timeService;
        this.patientRepository = patientRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new EntityNotFoundException("Invalid doctorId"));

        if (timeService.isLastClockApproaching(doctorId, 15)) {
            LocalDate currentDate = LocalDate.now();
            LocalDate futureDate = currentDate.plusDays(20);

            LocalDate lastClock = timeService.findLastClockByDoctorId(doctorId).toLocalDate();

            timeService.createDoctorClocks(doctor, lastClock.plusDays(1), futureDate);
            timeService.deleteOldClocks(doctorId, 10);
        }

        return doctor;
    }

    public Doctor getDoctorByTC(String doctorTC) {
        return getDoctorById(doctorRepository.findByTC(doctorTC).getId());
    }

    public Doctor createDoctor(DoctorRequest doctorRequest) {
        Doctor doctor = doctorRepository.findByTC(doctorRequest.getTC());
        Patient patient = patientRepository.findByTC(doctorRequest.getTC());
        if (doctor != null || patient != null) throw new EntityExistsException("There is already a person with this TC.");
        doctor = Doctor.builder().
                TC(doctorRequest.getTC()).
                name(doctorRequest.getName()).
                password(doctorRequest.getPassword()).
                specialty(doctorRequest.getSpecialty()).
                build();

        doctorRepository.save(doctor);

        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(20);

        timeService.createDoctorClocks(doctor, currentDate.plusDays(1), futureDate);

        return doctor;
    }

    public Doctor updateDoctorById(Long doctorId, DoctorRequest doctorRequest) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            Doctor foundDoctor = doctor.get();
            if (doctorRequest.getTC() != null) foundDoctor.setTC(doctorRequest.getTC());
            if (doctorRequest.getName() != null) foundDoctor.setName(doctorRequest.getName());
            if (doctorRequest.getPassword() != null) foundDoctor.setPassword(doctorRequest.getPassword());
            if (doctorRequest.getSpecialty() != null) foundDoctor.setSpecialty(doctorRequest.getSpecialty());
            return doctorRepository.save(foundDoctor);
        } else throw new EntityNotFoundException("Invalid doctorId");
    }

    public void deleteDoctorById(Long doctorId) {
        doctorRepository.deleteById(doctorId);
    }

}
