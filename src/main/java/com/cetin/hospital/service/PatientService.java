package com.cetin.hospital.service;

import com.cetin.hospital.model.Doctor;
import com.cetin.hospital.model.Patient;
import com.cetin.hospital.repository.DoctorRepository;
import com.cetin.hospital.repository.PatientRepository;
import com.cetin.hospital.request.PatientRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository, DoctorRepository doctorRepository, BCryptPasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Invalid patientId"));
    }

    public Patient getPatientByTC(String patientTC) {
        return patientRepository.findByTC(patientTC);
    }

    public Patient createPatient(PatientRequest patientRequest) {
        Patient patient = patientRepository.findByTC(patientRequest.getTC());
        Doctor doctor = doctorRepository.findByTC(patientRequest.getTC());
        if (patient != null || doctor != null) throw new EntityExistsException("There is already a person with this TC.");
        patient = Patient.builder().
                TC(patientRequest.getTC()).
                name(patientRequest.getName()).
                password(passwordEncoder.encode(patientRequest.getPassword())).
                build();
        return patientRepository.save(patient);
    }

    public Patient updatePatientById(Long patientId, PatientRequest patientRequest) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            Patient foundPatient = patient.get();
            if (patientRequest.getTC() != null) foundPatient.setTC(patientRequest.getTC());
            if (patientRequest.getName() != null) foundPatient.setName(patientRequest.getName());
            if (patientRequest.getPassword() != null) foundPatient.setPassword(patientRequest.getPassword());
            return patientRepository.save(foundPatient);
        } else throw new EntityNotFoundException("Invalid patientId");
    }

    public void deletePatientById(Long patientId) {
        patientRepository.deleteById(patientId);
    }
}
