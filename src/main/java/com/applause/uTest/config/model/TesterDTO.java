package com.applause.uTest.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TesterDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String country;
    private String lastLogin;
}
