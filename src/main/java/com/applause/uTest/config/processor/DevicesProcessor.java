package com.applause.uTest.config.processor;

import com.applause.uTest.config.model.DeviceDTO;

import org.springframework.batch.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DevicesProcessor implements ItemProcessor<DeviceDTO, DeviceDTO> {
    public DeviceDTO process(DeviceDTO deviceDTO) {
        log.debug("Inserting device : {}", deviceDTO);
        return deviceDTO;
    }
}
