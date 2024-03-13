package com.hospital.hospital.repository;

import com.hospital.hospital.model.Time;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<Time, Long> {
}
