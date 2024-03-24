package com.cetin.hospital.controller;

import com.cetin.hospital.model.Time;
import com.cetin.hospital.response.TimeResponse;
import com.cetin.hospital.service.TimeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/times")
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public List<TimeResponse> getTimesByDoctorId(@RequestParam Long doctorId) {
        List<Time> times = timeService.getTimesByDoctorId(doctorId);
        return times.stream().map(TimeResponse::new).toList();
    }

    @GetMapping("/{timeId}")
    public TimeResponse getTimeById(@PathVariable Long timeId) {
        return new TimeResponse(timeService.getTimeById(timeId));
    }
}
