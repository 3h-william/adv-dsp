package com.advdsp.service.manager;

import com.advdsp.service.manager.service.*;
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
    public static AdxConfigManagerService adxConfigManagerService;
    public static AppConfigService appConfigService;
    public static ChannelConfigService channelConfigService;
    public static CustomerConfigService customerConfigService;
    public static SystemConfigService systemConfigService;
    public static AdvConfigService advConfigService;
    public static AdvStatisticService advStatisticService;
    public static TokenService tokenService;
    public static UserService userService;

    static {
        adxConfigManagerService = (AdxConfigManagerService) applicationContext.getBean("adxConfigManagerService");
        appConfigService = (AppConfigService) applicationContext.getBean("appConfigService");
        channelConfigService = (ChannelConfigService) applicationContext.getBean("channelConfigService");
        customerConfigService  = (CustomerConfigService) applicationContext.getBean("customerConfigService");
        systemConfigService  = (SystemConfigService) applicationContext.getBean("systemConfigService");
        advConfigService  = (AdvConfigService) applicationContext.getBean("advConfigService");
        advStatisticService  = (AdvStatisticService) applicationContext.getBean("advStatisticService");
        tokenService = (TokenService) applicationContext.getBean("tokenService");
        userService = (UserService) applicationContext.getBean("userService");
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

}