package com.applause.uTest.controller;

import com.applause.uTest.service.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("api/countries")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("/unique")
    public List<String> fetchUniqueDevices() {
        return countryService.fetchUniqueCountries();
    }
}
