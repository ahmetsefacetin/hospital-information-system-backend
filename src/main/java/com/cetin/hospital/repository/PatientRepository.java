package com.cetin.hospital.repository;

import com.cetin.hospital.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByTC(String TC);
}
