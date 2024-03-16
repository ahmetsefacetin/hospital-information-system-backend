package com.cetin.hospital.service;

import com.cetin.hospital.model.Patient;
import com.cetin.hospital.repository.PatientRepository;
import com.cetin.hospital.request.PatientRequest;
import com.cetin.hospital.response.PatientResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponse> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientResponse::new).toList();
    }

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Invalid patientId"));
    }

    public Patient createPatient(PatientRequest patientRequest) {
        Patient patient = patientRepository.findByTC(patientRequest.getTC());
        if (patient != null) throw new EntityExistsException("There is already a patient with this TC.");
        patient = Patient.builder().
                TC(patientRequest.getTC()).
                name(patientRequest.getName()).
                password(patientRequest.getPassword()).
                build();
        return patientRepository.save(patient);
    }

    public Patient updatePatientById(Long patientId, PatientRequest patientRequest) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            Patient foundPatient = patient.get();
            foundPatient.setTC(patientRequest.getTC());
            foundPatient.setName(patientRequest.getName());
            foundPatient.setPassword(patientRequest.getPassword());
            return patientRepository.save(foundPatient);
        } else throw new EntityNotFoundException("Invalid patientId");
    }

    public void deletePatientById(Long patientId) {
        patientRepository.deleteById(patientId);
    }
}
