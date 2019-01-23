package com.pstickney.argos.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ProcessUtils
{
    private static Logger LOG = LogManager.getLogger(ProcessUtils.class);

    public static ProcessResult exec(String... cmds) throws IOException, InterruptedException
    {
        LOG.debug("EXEC PROCESS " + Arrays.toString(cmds));

        List<String> stdout = new ArrayList<>();
        List<String> stderr = new ArrayList<>();
        ProcessResult result = new ProcessResult();
        ProcessBuilder builder = new ProcessBuilder(cmds);

        Process process = builder.start();
        ProcessStream stdoutStream = new ProcessStream(process.getInputStream(), stdout::add);
        ProcessStream stderrStream = new ProcessStream(process.getErrorStream(), stderr::add);

        stdoutStream.start();
        stderrStream.start();

        int exit = process.waitFor();
        Thread.sleep(500);

        result.setExitStatus(exit);
        result.setStdout(stdout);
        result.setStderr(stderr);

        LOG.debug("EXEC PROCESS FINISHED");
        return result;
    }

}

class ProcessStream extends Thread
{
    private Logger LOG = LogManager.getLogger(ProcessStream.class);

    private InputStream in;
    private Consumer<String> consumer;

    ProcessStream(InputStream in, Consumer<String> consumer)
    {
        this.in = in;
        this.consumer = consumer;
    }

    @Override
    public void run()
    {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            reader.lines().forEach(consumer);
            reader.close();
        } catch (IOException e) {
            LOG.error(e);
        }
    }
}