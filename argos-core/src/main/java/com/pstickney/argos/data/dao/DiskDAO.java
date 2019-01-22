package com.pstickney.argos.data.dao;

import com.pstickney.argos.data.dto.DiskDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiskDAO extends JpaRepository<DiskDTO, Long>
{

}
