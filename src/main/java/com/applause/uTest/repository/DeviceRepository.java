package com.applause.uTest.repository;


import com.applause.uTest.domain.Device;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviceRepository extends CrudRepository<Device, Long> {
    @Query("SELECT DISTINCT d.description FROM Device d")
    List<String> findDistinctDevices();
}
