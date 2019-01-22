package com.pstickney.argos.service;

import com.pstickney.argos.data.dao.MemoryDAO;
import com.pstickney.argos.data.dto.MemoryDTO;
import com.pstickney.argos.dependency.DependencyFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Service
public class MemoryService extends AbstractService
{
    @Autowired
    private MemoryDAO memoryDAO;

    private Map<String, MemoryDTO> stats;
    private Logger LOG = LogManager.getLogger(MemoryService.class);

    public MemoryService()
    {
        stats = new HashMap<>();
        cmd = new String[]{"sh", "-c", "free"};
        dependencies = Collections.unmodifiableList(
            Arrays.asList(
                DependencyFactory.createPackageDependencyProcps()
            )
        );
    }

    @Override
    protected void before() {
        LOG.debug("{}.before()", getClass().getSimpleName());
        stats.clear();
        timestamp = Instant.now().getEpochSecond();
    }

    @Override
    protected boolean filter(String s) {
        LOG.debug("{}.filter({})", getClass().getSimpleName(), s);
        return s.startsWith("mem");
    }

    @Override
    protected void forEach(String s) {
        LOG.debug("{}.forEach({})", getClass().getSimpleName(), s);
        MemoryDTO stat = new MemoryDTO();
        StringTokenizer tokenizer = new StringTokenizer(s, " ");

        stat.setComputerId(compId);
        stat.setTimestamp(timestamp);

        stat.setDevice(tokenizer.nextToken().replaceAll(":", ""));
        stat.setTotal(Long.valueOf(tokenizer.nextToken()));
        stat.setUsed(Long.valueOf(tokenizer.nextToken()));
        stat.setFree(Long.valueOf(tokenizer.nextToken()));
        stat.setShared(Long.valueOf(tokenizer.nextToken()));
        stat.setCache(Long.valueOf(tokenizer.nextToken()));
        stat.setAvailable(Long.valueOf(tokenizer.nextToken()));

        LOG.debug(stat);
        stats.put(stat.getDevice(), stat);
    }

    @Override
    protected void after() {
        LOG.debug("{}.after()", getClass().getSimpleName());
        LOG.debug("stats: {}", stats);
//        memoryDAO.saveAll(stats.values());
    }
}
