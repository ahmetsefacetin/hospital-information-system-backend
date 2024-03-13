package com.cetin.hospital.service;

import com.cetin.hospital.repository.TimeRepository;
import org.springframework.stereotype.Service;

@Service
public class TimeService {
    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }
}
