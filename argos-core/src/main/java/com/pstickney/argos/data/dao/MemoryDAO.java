package com.pstickney.argos.data.dao;

import com.pstickney.argos.data.dto.MemoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryDAO extends JpaRepository<MemoryDTO, Long>
{

}
