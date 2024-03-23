package com.cetin.hospital.controller;

import com.cetin.hospital.model.Doctor;
import com.cetin.hospital.request.DoctorRequest;
import com.cetin.hospital.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody DoctorRequest doctorRequest) {
        Doctor doctor = doctorService.createDoctor(doctorRequest);
        if (doctor != null) {
            return new ResponseEntity<>(doctor, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
