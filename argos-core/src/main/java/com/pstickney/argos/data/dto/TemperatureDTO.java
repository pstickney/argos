package com.pstickney.argos.data.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "TEMPERATURE")
public class TemperatureDTO implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;
    private long timestamp = 0;
    private String computerId = "UNKNOWN";
    private String device = "UNKNOWN";
    private int value = 0;
}
