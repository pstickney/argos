package com.pstickney.argos.data.dao;

import com.pstickney.argos.data.dto.NetworkDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NetworkDAO extends JpaRepository<NetworkDTO, Long>
{

}
