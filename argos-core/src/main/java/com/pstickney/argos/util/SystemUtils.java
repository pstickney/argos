package com.pstickney.argos.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SystemUtils
{
    private static Logger LOG = LogManager.getLogger(SystemUtils.class);

    public static String getSystemName()
    {
        String[] cmd = {"sh", "-c", "uname -n"};
        ProcessResult result;

        try {
            result = ProcessUtils.exec(cmd);

            if( result.getExitStatus() != 0 ) {
                LOG.debug("Process returned non-0 exist status: {}", result.getExitStatus());
                return "UNKNOWN";
            }

            LOG.debug(result.toString());
            return result.getStdout().get(0);
        } catch (IOException e) {
            LOG.error(e);
        } catch (InterruptedException e) {
            LOG.error(e);
            Thread.currentThread().interrupt();
        }
        return "UNKNOWN";
    }
}
