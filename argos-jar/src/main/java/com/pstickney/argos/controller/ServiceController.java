package com.pstickney.argos.controller;

import com.pstickney.argos.config.ServiceConfig;
import com.pstickney.argos.service.*;
import com.pstickney.argos.util.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.UnknownServiceException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class ServiceController
{
    @Autowired private ServiceConfig serviceConfig;
    @Autowired private NetworkingService networkingService;
    @Autowired private CpuService cpuService;
    @Autowired private CpuTemperatureService cpuTemperatureService;
    @Autowired private DiskService diskService;
    @Autowired private DiskTemperatureService diskTemperatureService;
    @Autowired private MemoryService memoryService;

    private Logger LOG = LogManager.getLogger(ServiceController.class);
    private ScheduledExecutorService executor;
    private String compName;


    public ServiceController()
    {
        executor = Executors.newScheduledThreadPool(5);
    }

    public void startServices()
    {
        LOG.info("Services are being initialized");

        compName = SystemUtils.getSystemName();
        LOG.debug("System name: {}", compName);

        networkingService.init(compName);
        cpuService.init(compName);
        cpuTemperatureService.init(compName);
        diskService.init(compName);
        diskTemperatureService.init(compName);
        memoryService.init(compName);

        LOG.info("Services are being scheduled");
        try {
            executor.scheduleAtFixedRate(
                    () -> networkingService.run(),
                    serviceConfig.getServiceByName(NetworkingService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getDelay(),
                    serviceConfig.getServiceByName(NetworkingService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getPeriod(),
                    TimeUnit.SECONDS);

            executor.scheduleAtFixedRate(
                    () -> cpuService.run(),
                    serviceConfig.getServiceByName(CpuService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getDelay(),
                    serviceConfig.getServiceByName(CpuService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getPeriod(),
                    TimeUnit.SECONDS);

            executor.scheduleAtFixedRate(
                    () -> cpuTemperatureService.run(),
                    serviceConfig.getServiceByName(CpuTemperatureService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getDelay(),
                    serviceConfig.getServiceByName(CpuTemperatureService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getPeriod(),
                    TimeUnit.SECONDS);

            executor.scheduleAtFixedRate(
                    () -> diskService.run(),
                    serviceConfig.getServiceByName(DiskService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getDelay(),
                    serviceConfig.getServiceByName(DiskService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getPeriod(),
                    TimeUnit.SECONDS);

            executor.scheduleAtFixedRate(
                    () -> diskTemperatureService.run(),
                    serviceConfig.getServiceByName(DiskTemperatureService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getDelay(),
                    serviceConfig.getServiceByName(DiskTemperatureService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getPeriod(),
                    TimeUnit.SECONDS);

            executor.scheduleAtFixedRate(
                    () -> memoryService.run(),
                    serviceConfig.getServiceByName(MemoryService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getDelay(),
                    serviceConfig.getServiceByName(MemoryService.class.getSimpleName()).orElseThrow(UnknownServiceException::new).getPeriod(),
                    TimeUnit.SECONDS);
        } catch (UnknownServiceException e) {
            LOG.error(e);
        }
    }

    public void stopServices()
    {
        LOG.info("Monitors are being stopped");
        executor.shutdown();
    }
}
