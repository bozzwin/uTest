package com.applause.uTest.controller;

import com.applause.uTest.service.RankingTestersService;
import com.applause.uTest.service.ResultDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("api/testers")
public class RankingController {

    @Autowired
    private RankingTestersService service;

    @GetMapping("/ranking")
    public List<ResultDTO> search(@RequestParam(defaultValue = "ALL") List<String> countries, @RequestParam(defaultValue = "ALL") List<String> phones) {
        return service.generateRanking(countries, phones);
    }
}
