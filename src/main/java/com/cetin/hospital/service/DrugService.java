package com.cetin.hospital.service;

import com.cetin.hospital.model.Drug;
import com.cetin.hospital.model.Prescription;
import com.cetin.hospital.repository.DrugRepository;
import com.cetin.hospital.repository.PrescriptionRepository;
import com.cetin.hospital.request.DrugRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrugService {
    private final DrugRepository drugRepository;
    private final PrescriptionRepository prescriptionRepository;

    public DrugService(DrugRepository drugRepository, PrescriptionRepository prescriptionRepository) {
        this.drugRepository = drugRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }

    public List<Drug> getDrugsByPrescriptionId(Long prescriptionId) {
        return drugRepository.findByPrescriptionId(prescriptionId);
    }

    public Drug getDrugById(Long drugId) {
        return drugRepository.findById(drugId).orElseThrow(() -> new EntityNotFoundException("Invalid drugId"));
    }

    public Drug createDrug(DrugRequest drugRequest) {
        Prescription prescription = prescriptionRepository.findById(drugRequest.getPrescriptionId()).orElseThrow(() -> new EntityNotFoundException("Invalid prescriptionId"));
        Drug drug = Drug.builder().
                name(drugRequest.getName()).
                price(drugRequest.getPrice()).
                prescription(prescription).
                build();
        return drugRepository.save(drug);
    }

    public void deleteDrugById(Long drugId) {
        drugRepository.deleteById(drugId);
    }

}
