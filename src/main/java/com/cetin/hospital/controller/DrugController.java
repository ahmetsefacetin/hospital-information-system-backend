package com.cetin.hospital.controller;

import com.cetin.hospital.model.Drug;
import com.cetin.hospital.request.DrugRequest;
import com.cetin.hospital.response.DrugResponse;
import com.cetin.hospital.service.DrugService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/drugs")
public class DrugController {
    private final DrugService drugService;

    public DrugController(DrugService drugService) {
        this.drugService = drugService;
    }

    @GetMapping
    public List<DrugResponse> getAllDrugs() {
        List<Drug> drugs = drugService.getAllDrugs();
        return drugs.stream().map(DrugResponse::new).toList();
    }

    @GetMapping("/{drugId}")
    public DrugResponse getDrugById(@PathVariable Long drugId) {
        return new DrugResponse(drugService.getDrugById(drugId));
    }

    @PostMapping
    public ResponseEntity<Drug> createDrug(@RequestBody DrugRequest drugRequest) {
        Drug drug = drugService.createDrug(drugRequest);
        if (drug != null) {
            return new ResponseEntity<>(drug, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{drugId}")
    public ResponseEntity<Drug> updateDrugById(@PathVariable Long drugId, @RequestBody DrugRequest drugRequest) {
        Drug drug = drugService.updateDrugById(drugId, drugRequest);
        if (drug != null) {
            return new ResponseEntity<>(drug, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{drugId}")
    public void deleteDrugById(@PathVariable Long drugId) {
        drugService.deleteDrugById(drugId);
    }

}
