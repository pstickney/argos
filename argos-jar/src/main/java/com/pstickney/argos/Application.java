package com.pstickney.argos;

import com.pstickney.argos.config.ServiceConfig;
import com.pstickney.argos.controller.ServiceController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ServiceConfig.class)
public class Application implements CommandLineRunner
{
    private static Logger LOG = LogManager.getLogger(Application.class);

    @Autowired
    private ServiceController controller;

    private boolean running = true;

    public static void main(String... args)
    {
        LOG.info("STARTING SYSTEM MONITORING");
        new SpringApplicationBuilder()
                .sources(Application.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("STOPPING SYSTEM MONITORING");
            if( controller != null )
                controller.stopServices();
            running = false;
        }));

        controller.startServices();
        
        while(running) {
            Thread.sleep(1000);
        }
    }
}
