package com.applause.uTest.config.processor;

import com.applause.uTest.config.model.BugDTO;

import org.springframework.batch.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BugsProcessor implements ItemProcessor<BugDTO, BugDTO> {
    public BugDTO process(BugDTO bugDTO) {
        log.debug("Inserting bug : {}", bugDTO);
        return bugDTO;
    }
}
