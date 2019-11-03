package com.applause.uTest.config.processor;

import com.applause.uTest.config.model.TesterDTO;

import org.springframework.batch.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestersProcessor implements ItemProcessor<TesterDTO, TesterDTO> {
    public TesterDTO process(TesterDTO testerDTO) {
        log.debug("Inserting tester : {}", testerDTO);
        return testerDTO;
    }
}
