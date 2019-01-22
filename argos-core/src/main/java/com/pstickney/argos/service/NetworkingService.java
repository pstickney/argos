package com.pstickney.argos.service;

import com.pstickney.argos.data.dao.NetworkDAO;
import com.pstickney.argos.data.dto.NetworkDTO;
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
public class NetworkingService extends AbstractService
{
    @Autowired
    private NetworkDAO networkDAO;

    private Map<String, NetworkDTO> stats;
    private Logger LOG = LogManager.getLogger(NetworkingService.class);

    public NetworkingService()
    {
        stats = new HashMap<>();
        cmd = new String[]{"sh", "-c", "nicstat -n -s 1 2"};
        dependencies = Collections.unmodifiableList(
            Arrays.asList(
                DependencyFactory.createPackageDependencyNicstat()
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
        return !s.contains("time");
    }

    @Override
    protected void forEach(String s) {
        LOG.debug("{}.forEach({})", getClass().getSimpleName(), s);
        NetworkDTO stat = new NetworkDTO();
        StringTokenizer tokenizer = new StringTokenizer(s, " ");
        tokenizer.nextToken(); // consume timestamp

        stat.setTimestamp(timestamp);
        stat.setComputerId(compId);

        tokenizer.nextToken();
        stat.setDevice(tokenizer.nextToken());
        stat.setIn(Double.valueOf(tokenizer.nextToken()).intValue());
        stat.setOut(Double.valueOf(tokenizer.nextToken()).intValue());

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
