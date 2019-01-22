package com.pstickney.argos.data.dao;

import com.pstickney.argos.data.dto.CpuDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CpuDAO extends JpaRepository<CpuDTO, Long>
{
    List<CpuDTO> findByTimestampBetweenOrderByTimestampDesc(long start, long end);
    List<CpuDTO> findByComputerIdAndCpuIdAndTimestampBetweenOrderByTimestampDesc(String computerId, int cpuId, long start, long end);

//    public List<CpuDTO> getAllInRange(long start, long end)
//    {
//        return new ArrayList<>(dao.findByTimestampBetweenOrderByTimestampDesc(start, end));
//    }
//
//    public List<Double> getUsrStats(String computer, int id, long start, long end)
//    {
//        return new ArrayList<>(dao.findByComputerIdAndCpuIdAndTimestampBetweenOrderByTimestampDesc(computer, id, start, end)).stream()
//                .map(CpuDTO::getUsr)
//                .collect(Collectors.toList());
//    }
}
