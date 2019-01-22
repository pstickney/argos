package com.pstickney.argos.dependency;

import com.pstickney.argos.util.ProcessUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class PackageDependency implements Dependency
{
    private Logger LOG = LogManager.getLogger(PackageDependency.class);
    private String pkg;
    private String[] dpkgCmd;

    public PackageDependency(String pkg) {
        this.pkg = pkg;
        this.dpkgCmd = new String[] {"sh", "-c", "dpkg -l " + pkg};
    }

    @Override
    public boolean exists() {
        try {
            return ProcessUtils.exec(dpkgCmd).getExitStatus() == 0;
        } catch (IOException e) {
            LOG.error(e);
        } catch (InterruptedException e) {
            LOG.error(e);
            Thread.currentThread().interrupt();
        }
        return false;
    }

    public String getPkg() {
        return pkg;
    }

    public PackageDependency setPkg(String pkg) {
        this.pkg = pkg;
        return this;
    }
}
