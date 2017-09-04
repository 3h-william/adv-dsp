package com.advdsp.service.dsp.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader
{
    private static Logger logger = LoggerFactory.getLogger(PropertyLoader.class.getName());
    private Properties prop;
    private static PropertyLoader servicePropertyLoaderInstance = new PropertyLoader();

    public void config(Properties properties)
    {
        if (null != properties) {
            prop = properties;
        }
    }

    public static PropertyLoader getServerPropertyInstance()
    {
        return PropertyLoader.servicePropertyLoaderInstance;
    }

    public long getLong(String key, long defaultLong)
    {
        if (prop.containsKey(key)) {
            return Long.parseLong(prop.getProperty(key));
        }
        else {
            return defaultLong;
        }
    }

    public long getLong(String key)
    {
        if (prop.containsKey(key)) {
            return Long.parseLong(prop.getProperty(key));
        }
        else {
            throw new RuntimeException("key is not exist in properties : " + key);
        }
    }

    public int getInt(String key, int defaultInt)
    {
        if (prop.containsKey(key)) {
            return Integer.parseInt(prop.getProperty(key));
        }
        else {
            return defaultInt;
        }
    }

    public int getInt(String key)
    {
        if (prop.containsKey(key)) {
            return Integer.parseInt(prop.getProperty(key));
        }
        else {
            throw new RuntimeException("key is not exist in properties : " + key);
        }
    }

    public String getValue(String key, String defaultString)
    {
        return this.prop.getProperty(key, defaultString);
    }

    public String getValue(String key)
    {
        return this.prop.getProperty(key);
    }

    public void setProp(Properties prop)
    {
        this.prop = prop;
    }

    public static Properties getPropertiesFromClasspath(String propFileName)
    {
        logger.info("load properties from classpath:" + propFileName);
        InputStream is = PropertyLoader.class.getClassLoader().getResourceAsStream(propFileName);
        if (null == is) {
            System.err.println(propFileName + " not found , exit system");
            System.exit(-1);
        }
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(is);
            logger.info("load over");
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error("load " + propFileName + " error , exit syetem");
            System.exit(-1);
        }
        return properties;
    }
}
