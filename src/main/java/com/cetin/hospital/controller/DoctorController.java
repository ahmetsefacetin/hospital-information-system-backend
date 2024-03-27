package com.cetin.hospital.controller;

import com.cetin.hospital.model.Doctor;
import com.cetin.hospital.request.DoctorRequest;
import com.cetin.hospital.response.DoctorResponse;
import com.cetin.hospital.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return doctors.stream().map(DoctorResponse::new).toList();
    }

    @GetMapping("/{doctorId}")
    public DoctorResponse getDoctorById(@PathVariable Long doctorId) {
        return new DoctorResponse(doctorService.getDoctorById(doctorId));
    }

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody DoctorRequest doctorRequest) {
        Doctor doctor = doctorService.createDoctor(doctorRequest);
        if (doctor != null) {
            return new ResponseEntity<>(doctor, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{doctorId}")
    public ResponseEntity<Doctor> updateDoctorById(@PathVariable Long doctorId, @RequestBody DoctorRequest doctorRequest) {
        Doctor doctor = doctorService.updateDoctorById(doctorId, doctorRequest);
        if (doctor != null) {
            return new ResponseEntity<>(doctor, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{doctorId}")
    public void deleteDoctorById(@PathVariable Long doctorId) {
        doctorService.deleteDoctorById(doctorId);
    }
}
