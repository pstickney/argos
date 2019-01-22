package com.pstickney.argos.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ProcessUtils
{
    private static Logger LOG = LogManager.getLogger(ProcessUtils.class);
    private static Runtime runtime = Runtime.getRuntime();

    public static ProcessResult exec(String... cmds) throws IOException, InterruptedException
    {
        LOG.debug("EXEC PROCESS " + Arrays.toString(cmds));

        Internals internals = new Internals();
        ProcessResult result = new ProcessResult();

        Process process = runtime.exec(cmds);
        ProcessStream stdout = new ProcessStream(process.getInputStream(), internals.stdout);
        ProcessStream stderr = new ProcessStream(process.getErrorStream(), internals.stderr);

        stdout.start();
        stderr.start();

        int exit = process.waitFor();

        result.setExitStatus(exit);
        result.setStdout(internals.stdout);
        result.setStderr(internals.stderr);

        LOG.debug("EXEC PROCESS FINISHED");
        return result;
    }

}

class Internals
{
    List<String> stdout;
    List<String> stderr;

    Internals()
    {
        stdout = new LinkedList<>();
        stderr = new LinkedList<>();
    }
}

class ProcessStream extends Thread
{
    private Logger LOG = LogManager.getLogger(ProcessStream.class);
    private InputStream in;
    private List<String> list;
    private BufferedReader reader;

    ProcessStream(InputStream in, List<String> list)
    {
        this.in = in;
        this.list = list;
    }

    @Override
    public void run()
    {
        String line;
        reader = new BufferedReader(new InputStreamReader(in));

        try {
            while( (line = reader.readLine()) != null )
                list.add(line.trim());
        } catch (IOException e) {
            LOG.error(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                LOG.error(e);
            }
        }
    }
}