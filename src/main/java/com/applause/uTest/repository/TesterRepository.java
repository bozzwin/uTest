package com.applause.uTest.repository;

import com.applause.uTest.domain.Tester;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TesterRepository extends CrudRepository<Tester, Long> {
    @Query("SELECT DISTINCT t.country FROM Tester t")
    List<String> findDistinctCountries();

    List<Tester> findByCountryIn(List<String> countries);

    @Override
    List<Tester> findAll();
}
