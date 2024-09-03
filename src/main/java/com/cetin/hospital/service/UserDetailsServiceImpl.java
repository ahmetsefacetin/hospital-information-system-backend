package com.cetin.hospital.service;

import com.cetin.hospital.enumeration.Role;
import com.cetin.hospital.model.Doctor;
import com.cetin.hospital.model.Patient;
import com.cetin.hospital.repository.PatientRepository;
import com.cetin.hospital.repository.DoctorRepository;
import com.cetin.hospital.security.JwtUserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public UserDetailsServiceImpl(PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(String TC) throws UsernameNotFoundException {

        Patient patient = patientRepository.findByTC(TC);
        if(patient != null){
            return new JwtUserDetails(patient.getId(), patient.getTC(),
                    patient.getPassword(), Role.PATIENT);
        }

        Doctor doctor = doctorRepository.findByTC(TC);
        if(doctor != null){
            return new JwtUserDetails(doctor.getId(), doctor.getTC(),
                    doctor.getPassword(), Role.DOCTOR);
        }

        throw new UsernameNotFoundException("Invalid TC");
    }

    public JwtUserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {

        Optional<Patient> found = patientRepository.findById(userId);
        if(found.isPresent()){
            Patient patient = found.get();
            return new JwtUserDetails(patient.getId(), patient.getTC(),
                    patient.getPassword(), Role.PATIENT);
        }

        Optional<Doctor> found2 = doctorRepository.findById(userId);
        if(found2.isPresent()){
            Doctor doctor = found2.get();
            return new JwtUserDetails(doctor.getId(), doctor.getTC(),
                    doctor.getPassword(), Role.DOCTOR);
        }

        throw new BadCredentialsException("Invalid userId");
    }
}