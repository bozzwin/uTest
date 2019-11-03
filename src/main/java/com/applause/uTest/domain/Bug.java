package com.applause.uTest.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bugs")
public class Bug {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long bugid;
    private Long testerid;
    private Long deviceid;
}
