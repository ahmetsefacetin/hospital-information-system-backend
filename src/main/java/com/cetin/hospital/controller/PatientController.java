package com.cetin.hospital.controller;

import com.cetin.hospital.model.Patient;
import com.cetin.hospital.request.PatientRequest;
import com.cetin.hospital.response.PatientResponse;
import com.cetin.hospital.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/patients")
@CrossOrigin(origins = "*")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/all")
    public List<PatientResponse> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return patients.stream().map(PatientResponse::new).toList();
    }

    @GetMapping("/{patientId}")
    public PatientResponse getPatientById(@PathVariable Long patientId) {
        return new PatientResponse(patientService.getPatientById(patientId));
    }

    @GetMapping
    public PatientResponse getPatientById(@RequestParam String TC) {
        return new PatientResponse(patientService.getPatientByTC(TC));
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody PatientRequest patientRequest) {
        Patient patient = patientService.createPatient(patientRequest);
        if (patient != null) {
            return new ResponseEntity<>(patient, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<Patient> updatePatientById(@PathVariable Long patientId, @RequestBody PatientRequest patientRequest) {
        Patient patient = patientService.updatePatientById(patientId, patientRequest);
        if (patient != null) {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{patientId}")
    public void deletePatientById(@PathVariable Long patientId) {
        patientService.deletePatientById(patientId);
    }

}
