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
@Table(name = "MEMORY")
public class MemoryDTO implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;
    private long timestamp = 0;
    private String computerId = "UNKNOWN";
    private String device = "UNKNOWN";
    private long total = 0;
    private long used = 0;
    private long free = 0;
    private long shared = 0;
    private long cache = 0;
    private long available = 0;
}
