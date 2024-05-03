package com.cetin.hospital.repository;

import com.cetin.hospital.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientTCAndDoctorTC(String patientTC, String DoctorTC);
    List<Prescription> findByPatientTC(String patientTC);
    List<Prescription> findByDoctorTC(String doctorTC);
}
