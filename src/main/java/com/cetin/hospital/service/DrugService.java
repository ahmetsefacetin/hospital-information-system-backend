package com.cetin.hospital.service;

import com.cetin.hospital.repository.DrugRepository;
import org.springframework.stereotype.Service;

@Service
public class DrugService {
    private final DrugRepository drugRepository;

    public DrugService(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }
}
