package com.applause.uTest.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TesterDeviceDTO {
    private int testerId;
    private int deviceId;
}
