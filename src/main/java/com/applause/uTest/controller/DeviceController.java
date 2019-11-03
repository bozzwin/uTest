package com.applause.uTest.controller;

import com.applause.uTest.service.DeviceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/unique")
    public List<String> fetchUniqueDevices() {
        return deviceService.fetchUniqueDevices();
    }
}
