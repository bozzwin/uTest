package com.applause.uTest.service;

import com.applause.uTest.repository.TesterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private TesterRepository testerRepository;

    @Autowired
    public CountryService(TesterRepository testerRepository){
        this.testerRepository = testerRepository;
    }

    public List<String> fetchUniqueCountries() {
        return testerRepository.findDistinctCountries();
    }
}
