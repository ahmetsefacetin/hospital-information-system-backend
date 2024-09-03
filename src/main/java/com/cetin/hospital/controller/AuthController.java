package com.cetin.hospital.controller;

import com.cetin.hospital.request.DoctorRequest;
import com.cetin.hospital.request.LoginRequest;
import com.cetin.hospital.request.PatientRequest;
import com.cetin.hospital.request.RefreshTokenRequest;
import com.cetin.hospital.response.AuthResponse;
import com.cetin.hospital.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/register/patient")
    public ResponseEntity<AuthResponse> register(@RequestBody PatientRequest patientRequest){
        return authService.registerPatient(patientRequest);
    }
    @PostMapping("/register/doctor")
    public ResponseEntity<AuthResponse> register(@RequestBody DoctorRequest doctorRequest){
        return authService.registerDoctor(doctorRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refresh(refreshTokenRequest);
    }

}
