package com.pstickney.argos.dependency;

import com.pstickney.argos.util.ProcessResult;
import com.pstickney.argos.util.ProcessUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class PackageDependency extends Dependency
{
    private Logger LOG = LogManager.getLogger(PackageDependency.class);

    public PackageDependency(String pkg) {
        this.setPkgName(pkg);
        this.setPkgCmd(new String[] {"sh", "-c", "dpkg -l " + pkg});
    }

    @Override
    public boolean exists()
    {
        ProcessResult result;

        try {
            result = ProcessUtils.exec(getPkgCmd());

            LOG.debug(result.toString());
            return result.getExitStatus() != 0;
        } catch (IOException e) {
            LOG.error(e);
        } catch (InterruptedException e) {
            LOG.error(e);
            Thread.currentThread().interrupt();
        }
        return false;
    }
}
