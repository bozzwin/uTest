package com.applause.uTest.config.processor;

import com.applause.uTest.config.model.TesterDeviceDTO;

import org.springframework.batch.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TesterDeviceProcessor implements ItemProcessor<TesterDeviceDTO, TesterDeviceDTO> {
    public TesterDeviceDTO process(TesterDeviceDTO testerDeviceDTO) {
        log.debug("Inserting testerDevice : {}", testerDeviceDTO);
        return testerDeviceDTO;
    }
}
