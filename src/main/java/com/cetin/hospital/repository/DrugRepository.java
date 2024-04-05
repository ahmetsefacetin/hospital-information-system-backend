package com.cetin.hospital.repository;

import com.cetin.hospital.model.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugRepository extends JpaRepository<Drug, Long> {
    Drug findByName(String name);
}
