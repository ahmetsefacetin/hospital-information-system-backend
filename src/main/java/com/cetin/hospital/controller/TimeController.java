package com.cetin.hospital.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/times")
public class TimeController {
    private final TimeController timeController;

    public TimeController(TimeController timeController) {
        this.timeController = timeController;
    }
}
