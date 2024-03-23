package com.cetin.hospital.controller;

import com.cetin.hospital.model.Time;
import com.cetin.hospital.response.TimeResponse;
import com.cetin.hospital.service.TimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/times")
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/{doctorId}")
    public List<TimeResponse> getTimesByDoctorId(@PathVariable Long doctorId) {
        List<Time> times = timeService.getTimesByDoctorId(doctorId);
        return times.stream().map(TimeResponse::new).toList();
    }
}
