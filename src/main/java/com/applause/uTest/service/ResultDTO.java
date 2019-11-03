package com.applause.uTest.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultDTO {
    private String testerName;
    private long score;
}
