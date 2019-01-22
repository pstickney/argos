package com.pstickney.argos.service;

import com.pstickney.argos.data.dao.CpuDAO;
import com.pstickney.argos.data.dto.CpuDTO;
import com.pstickney.argos.dependency.DependencyFactory;
import org.apache.commons.math3.util.Precision;
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
public class CpuService extends AbstractService
{
    @Autowired
    private CpuDAO cpuDAO;

    private Map<Integer, CpuDTO> stats;
    private Logger LOG = LogManager.getLogger(CpuService.class);

    public CpuService()
    {
        stats = new HashMap<>();
        cmd = new String[]{"sh", "-c", "mpstat -P ALL 1 1"};
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
        return s.startsWith("average:") && !s.contains("cpu") && !s.contains("all");
    }

    @Override
    public void forEach(String s) {
        LOG.debug("{}.forEach({})", getClass().getSimpleName(), s);
        CpuDTO stat = new CpuDTO();
        StringTokenizer tokenizer = new StringTokenizer(s, " ");

        stat.setComputerId(compId);
        stat.setTimestamp(timestamp);
        tokenizer.nextToken(); // consume average
        stat.setCpuId(Integer.valueOf(tokenizer.nextToken()));
        stat.setUsr(Precision.round(new Double(tokenizer.nextToken()), 1));
        stat.setNice(Precision.round(new Double(tokenizer.nextToken()), 1));
        stat.setSys(Precision.round(new Double(tokenizer.nextToken()), 1));
        stat.setIowait(Precision.round(new Double(tokenizer.nextToken()), 1));
        stat.setIrq(Precision.round(new Double(tokenizer.nextToken()), 1));
        stat.setSoft(Precision.round(new Double(tokenizer.nextToken()), 1));
        stat.setSteal(Precision.round(new Double(tokenizer.nextToken()), 1));

        LOG.debug(stat);
        stats.put(stat.getCpuId(), stat);
    }

    @Override
    protected void after() {
        LOG.debug("{}.after()", getClass().getSimpleName());
        LOG.debug("stats: {}", stats);
//        cpuDAO.saveAll(stats.values());
    }
}
