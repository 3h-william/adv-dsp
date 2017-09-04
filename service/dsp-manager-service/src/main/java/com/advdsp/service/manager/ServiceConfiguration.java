package com.advdsp.service.manager;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class ServiceConfiguration
{
    private static Logger logger =  LoggerFactory.getLogger(ServiceConfiguration.class.getName());

    private final String SERVICE_CONF_NAME = "dsp-manager-service.properties";

    private Configuration config;

    private static class ServiceConfigurationHandler {
        public static ServiceConfiguration serviceConfiguration = new ServiceConfiguration();

    }

    public static ServiceConfiguration getInstance() {
        return ServiceConfigurationHandler.serviceConfiguration;
    }


    public ServiceConfiguration() {
        try {
            config = new PropertiesConfiguration(SERVICE_CONF_NAME);
        } catch (ConfigurationException e) {
            logger.error("[ServiceConfiguration] init error ,exist system!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Configuration getConfig() {
        return config;
    }


}