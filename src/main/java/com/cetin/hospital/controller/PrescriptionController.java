package com.cetin.hospital.controller;

import com.cetin.hospital.model.Prescription;
import com.cetin.hospital.request.PrescriptionRequest;
import com.cetin.hospital.response.PrescriptionResponse;
import com.cetin.hospital.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/prescriptions")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/all")
    public List<PrescriptionResponse> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        return prescriptions.stream().map(PrescriptionResponse::new).toList();
    }

    @GetMapping("/{prescriptionId}")
    public PrescriptionResponse getPrescriptionById(@PathVariable Long prescriptionId) {
        return new PrescriptionResponse(prescriptionService.getPrescriptionById(prescriptionId));
    }

    @GetMapping
    public List<PrescriptionResponse> getPrescriptionByTC(@RequestParam Optional<String> patientTC,
                                                          @RequestParam Optional<String> doctorTC) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByTC(patientTC, doctorTC);
        return prescriptions.stream().map(PrescriptionResponse::new).toList();
    }

    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody PrescriptionRequest prescriptionRequest) {
        Prescription prescription = prescriptionService.createPrescription(prescriptionRequest);
        if (prescription != null) {
            return new ResponseEntity<>(prescription, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
