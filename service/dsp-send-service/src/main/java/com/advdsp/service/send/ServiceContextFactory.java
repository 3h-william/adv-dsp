package com.advdsp.service.send;

import com.advdsp.service.send.service.AdxConfigService;
import com.advdsp.service.send.service.AppBaseConfigService;
import com.advdsp.service.send.service.SystemConfigService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * manually maintain spring bean & service
 *
 */
public class ServiceContextFactory {

    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-mybatis.xml");
    private static AppBaseConfigService appConfigService;
    private static SystemConfigService systemConfigService;
    private static AdxConfigService adxConfigService;

    static {
        appConfigService = (AppBaseConfigService) applicationContext.getBean("appBaseConfigService");
        systemConfigService = (SystemConfigService) applicationContext.getBean("systemConfigService");
        adxConfigService = (AdxConfigService) applicationContext.getBean("adxConfigService");
    }

    public static AppBaseConfigService getAppConfigService() {
        return appConfigService;
    }

    public static SystemConfigService getSystemConfigService() {
        return systemConfigService;
    }

    public static AdxConfigService getAdxConfigService() {
        return adxConfigService;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

}