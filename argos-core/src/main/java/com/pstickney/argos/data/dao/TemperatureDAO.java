package com.pstickney.argos.data.dao;

import com.pstickney.argos.data.dto.TemperatureDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureDAO extends JpaRepository<TemperatureDTO, Long>
{

}
