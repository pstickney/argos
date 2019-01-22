package com.pstickney.argos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("config")
public class ServiceConfig
{
    private String name;
    private String environment;
    private String devices;
    private List<Service> services;

    public Optional<Service> getServiceByName(String name)
    {
        return getServices().stream()
                .filter((service -> service.getName().equalsIgnoreCase(name)))
                .findFirst();
    }

    @Data
    public static class Service
    {
        private String name;
        private int delay;
        private int period;
    }
}
