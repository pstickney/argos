package com.pstickney.argos.service;

import com.pstickney.argos.data.dao.DiskDAO;
import com.pstickney.argos.data.dto.DiskDTO;
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
public class DiskService extends AbstractService
{
    @Autowired
    private DiskDAO diskDAO;

    private Map<String, DiskDTO> stats;
    private Logger LOG = LogManager.getLogger(DiskService.class);

    public DiskService()
    {
        stats = new HashMap<>();
        cmd = new String[]{"sh", "-c", "iostat -d -y 1 1"};
        dependencies = Collections.unmodifiableList(
            Arrays.asList(
                DependencyFactory.createPackageDependencySysstat()
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
    public boolean filter(String s) {
        LOG.debug("{}.filter({})", getClass().getSimpleName(), s);
        return s.startsWith("sd");
    }

    @Override
    public void forEach(String s) {
        LOG.debug("{}.forEach({})", getClass().getSimpleName(), s);
        DiskDTO disk = new DiskDTO();
        StringTokenizer tokenizer = new StringTokenizer(s, " ");

        disk.setTimestamp(timestamp);
        disk.setComputerId(compId);
        disk.setDevice(tokenizer.nextToken());
        disk.setTps(Double.valueOf(tokenizer.nextToken()).intValue());
        disk.setRead(Double.valueOf(tokenizer.nextToken()).intValue());
        disk.setWrite(Double.valueOf(tokenizer.nextToken()).intValue());

        LOG.debug(disk);
        stats.put(disk.getDevice(), disk);
    }

    @Override
    protected void after() {
        LOG.debug("{}.after()", getClass().getSimpleName());
        LOG.debug("stats: {}", stats);
//        diskDAO.saveAll(stats.values());
    }
}
