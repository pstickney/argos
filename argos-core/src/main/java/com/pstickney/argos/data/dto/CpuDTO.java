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
@Table(name = "CPU")
public class CpuDTO implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;
    private long timestamp = 0;
    private String computerId = "UNKNOWN";
    private int cpuId = -1;
    private double usr = 0;
    private double nice = 0;
    private double sys = 0;
    private double iowait = 0;
    private double irq = 0;
    private double soft = 0;
    private double steal = 0;
}
