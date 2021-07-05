package com.environment;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
public class ChromeDriverManager extends DriverManager {
    @Override
    public void createDriver() {
        driver = new ChromeDriver();
        log.info("Maximize windows");
        driver.manage().window().maximize();
    }
}
