package com.environment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DriverManagerFactory {
    private static final String BROWSER_NAME = System.getProperty("browser", "chrome");

    public static DriverManager getDriverManager() {
        DriverDownloadManager.downloadDriver();
        DriverManager driverManager;
        switch (BROWSER_NAME) {
            case "chrome": {
                driverManager = new ChromeDriverManager();
                break;
            }
            default: {
                driverManager = new ChromeDriverManager();
                break;
            }
        }
        return driverManager;
    }
}
