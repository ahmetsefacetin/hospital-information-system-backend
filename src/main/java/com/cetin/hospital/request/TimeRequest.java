package com.cetin.hospital.request;

import com.cetin.hospital.model.Doctor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeRequest {
    private LocalDateTime time;
    private Doctor doctor;
}
