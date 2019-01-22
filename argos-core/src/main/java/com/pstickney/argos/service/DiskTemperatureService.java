package com.pstickney.argos.service;

import com.pstickney.argos.config.ServiceConfig;
import com.pstickney.argos.data.dao.TemperatureDAO;
import com.pstickney.argos.data.dto.TemperatureDTO;
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
public class DiskTemperatureService extends AbstractService
{
    @Autowired private ServiceConfig serviceConfig;
    @Autowired private TemperatureDAO temperatureDAO;

    private Map<String, TemperatureDTO> stats;
    private Logger LOG = LogManager.getLogger(DiskTemperatureService.class);

    public DiskTemperatureService()
    {
        stats = new HashMap<>();
        cmd = new String[]{"sh", "-c", "hddtemp /dev/sd[b-z]"}; // + serviceConfig.getDevices()};
        dependencies = Collections.unmodifiableList(
            Arrays.asList(
                DependencyFactory.createPackageDependencyHddtemp()
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
        return s.startsWith("core") && !s.contains("coretemp");
    }

    @Override
    protected void forEach(String s) {
        LOG.debug("{}.forEach({})", getClass().getSimpleName(), s);
        TemperatureDTO stat = new TemperatureDTO();
        StringTokenizer tokenizer = new StringTokenizer(s, ":");

        stat.setTimestamp(timestamp);
        stat.setComputerId(compId);

        stat.setDevice(tokenizer.nextToken());
        tokenizer.nextToken();
        stat.setValue(Integer.valueOf(tokenizer.nextToken()));

        LOG.debug(stat);
        stats.put(stat.getDevice(), stat);
    }

    @Override
    protected void after() {
        LOG.debug("{}.after()", getClass().getSimpleName());
        LOG.debug("stats: {}", stats);
//        networkDAO.saveAll(stats.values());
    }
}
