package com.cetin.hospital.repository;

import com.cetin.hospital.model.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrugRepository extends JpaRepository<Drug, Long> {
    List<Drug> findByPrescriptionId(Long prescriptionId);
}
