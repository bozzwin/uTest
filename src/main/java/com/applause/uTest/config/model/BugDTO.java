package com.applause.uTest.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BugDTO {
    private int bugId;
    private int deviceId;
    private int testerId;


}
