package com.pstickney.argos.service;

import com.pstickney.argos.dependency.Dependency;
import com.pstickney.argos.exception.UnsatisfiedDependencyException;
import com.pstickney.argos.util.ProcessResult;
import com.pstickney.argos.util.ProcessUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public abstract class AbstractService implements Runnable
{
    private Logger LOG = LogManager.getLogger(AbstractService.class);

    protected long timestamp;
    protected String compId;
    protected String[] cmd;
    protected List<Dependency> dependencies;

    protected abstract void before();
    protected abstract boolean filter(String s);
    protected abstract void forEach(String s);
    protected abstract void after();

    public void init(String id)
    {
        if( !hasAllDependencies() )
            throw new UnsatisfiedDependencyException("Missing dependency");
        compId = id;
    }

    protected boolean hasAllDependencies()
    {
        return dependencies.stream()
                .map(Dependency::exists)
                .reduce(true, (result, exists) -> result && exists);
    }

    @Override
    public void run()
    {
        LOG.debug("STARTING RUN ON {}", getClass().getSimpleName());

        try {
            ProcessResult result = ProcessUtils.exec(cmd);

            if( result.getExitStatus() != 0 ) {
                LOG.debug("Process returned a non-0 error code: {}", result.getExitStatus());
                LOG.debug("ENDING RUN ON {}", getClass().getSimpleName());
                return;
            }

            before();

            result.getStdout().stream()
                .map(line -> line.toLowerCase().trim())
                .filter(this::filter)
                .forEach(this::forEach);

            after();
        } catch (IOException e) {
            LOG.error(e);
        } catch (InterruptedException e) {
            LOG.error(e);
            Thread.currentThread().interrupt();
        }

        LOG.debug("ENDING RUN ON {}", getClass().getSimpleName());
    }
}
