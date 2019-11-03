package com.applause.uTest.service;

import com.applause.uTest.domain.Tester;
import com.applause.uTest.repository.BugRepository;
import com.applause.uTest.repository.TesterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Service
public class RankingTestersService {

    private static final String SELECT_ALL = "ALL";
    private TesterRepository testerRepository;
    private BugRepository bugRepository;

    @Autowired
    public RankingTestersService(TesterRepository testerRepository, BugRepository bugRepository) {
        this.testerRepository = testerRepository;
        this.bugRepository = bugRepository;
    }

    public List<ResultDTO> generateRanking(List<String> countries, List<String> typesOfPhones) {
        return findTesterByCountriesAndPhones(countries, typesOfPhones)
            .stream()
            .map(ranking(typesOfPhones))
            .sorted(Comparator.comparing(ResultDTO::getScore).reversed())
            .collect(toList());
    }

    private Function<Tester, ResultDTO> ranking(List<String> typesOfPhones) {
        return t -> {
            List<Long> deviceIds;
            if (typesOfPhones.contains(SELECT_ALL)) {
                deviceIds = t.getDevices()
                    .stream()
                    .map(e -> e.getId())
                    .collect(toList());
            } else {
                deviceIds = t.getDevices()
                    .stream()
                    .filter(e -> typesOfPhones.contains(e.getDescription()))
                    .map(e -> e.getId())
                    .collect(toList());
            }
            long score = bugRepository.countByTesteridAndDeviceidIn(t.getId(), deviceIds);
            return new ResultDTO(t.getFirstName(), score);
        };
    }

    private List<Tester> findTesterByCountriesAndPhones(List<String> countries, List<String> typesOfPhones) {

        List<Tester> testers;

        if (countries.contains(SELECT_ALL)) {
            testers = testerRepository.findAll();
        } else {
            testers = testerRepository.findByCountryIn(countries);
        }

        if (typesOfPhones.contains(SELECT_ALL)) {
            return testers;
        } else {
            return testers.stream()
                .filter(t -> t.getDevices()
                    .stream()
                    .anyMatch(p -> typesOfPhones.contains(p.getDescription())))
                    .collect(toList());
        }
    }
}
