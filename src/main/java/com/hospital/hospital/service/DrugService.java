package com.hospital.hospital.service;

import com.hospital.hospital.repository.DrugRepository;
import org.springframework.stereotype.Service;

@Service
public class DrugService {
    private final DrugRepository drugRepository;

    public DrugService(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }
}
