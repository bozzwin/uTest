package com.applause.uTest.repository;

import com.applause.uTest.domain.Bug;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BugRepository extends CrudRepository<Bug, Long> {

    int countByTesteridAndDeviceidIn(Long testerId, List<Long> deviceIds);
}
