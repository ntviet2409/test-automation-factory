package com.utilities;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

@Slf4j
public class PropertiesHelper {
    private static final String CONFIG_PROPS_PATH = System.getProperty("user.dir") + "/config.properties";
    private static final String DATA_PROPS_PATH = System.getProperty("user.dir") +"/src/test/resources/" + getConfigProperty("locale") + ".properties";

    public static String getConfigProperty(String propName) {
        return getProperty(CONFIG_PROPS_PATH, propName);
    }

    public static String getTestDataProperty(String propName) {
        return getProperty(DATA_PROPS_PATH, propName);
    }

    public static String getProperty(String fileName, String propName) {
        Properties prop = new Properties();
        InputStream is;
        try {
            is = new FileInputStream(fileName);
            prop.load(is);
            String property = prop.getProperty(propName);
            is.close();
            return property;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Properties loadLocalePropertiesFile() {
        String filePath = System.getProperty("user.dir") +"/src/test/resources/" + getConfigProperty("locale") + ".properties";
        return loadPropertiesFile(filePath);
    }

    public static Properties loadPropertiesFile(String filePath) {
        Properties props = new Properties();
        try {
            log.info("Load properties file path: " + filePath);
            props.load(new FileInputStream(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }
}
