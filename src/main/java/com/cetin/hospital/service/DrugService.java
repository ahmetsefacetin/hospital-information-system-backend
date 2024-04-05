package com.cetin.hospital.service;

import com.cetin.hospital.model.Drug;
import com.cetin.hospital.repository.DrugRepository;
import com.cetin.hospital.request.DrugRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DrugService {
    private final DrugRepository drugRepository;

    public DrugService(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }

    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }

    public Drug getDrugById(Long drugId) {
        return drugRepository.findById(drugId).orElseThrow(() -> new EntityNotFoundException("Invalid drugId"));
    }

    public Drug createDrug(DrugRequest drugRequest) {
        Drug drug = drugRepository.findByName(drugRequest.getName());
        if (drug != null) throw new EntityExistsException("There is already a drug with this name.");
        drug = Drug.builder().
                name(drugRequest.getName()).
                price(drugRequest.getPrice()).
                build();
        return drugRepository.save(drug);
    }

    public Drug updateDrugById(Long drugId, DrugRequest drugRequest) {
        Optional<Drug> drug = drugRepository.findById(drugId);
        if (drug.isPresent()) {
            Drug foundDrug = drug.get();
            if (drugRequest.getName() != null) foundDrug.setName(drugRequest.getName());
            if (drugRequest.getPrice() != null) foundDrug.setPrice(drugRequest.getPrice());
            return drugRepository.save(foundDrug);
        } else throw new EntityNotFoundException("Invalid drugId");
    }

    public void deleteDrugById(Long drugId) {
        drugRepository.deleteById(drugId);
    }

}
