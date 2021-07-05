package com.environment;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

@Slf4j
public abstract class DriverManager {
    WebDriver driver;
    protected abstract void createDriver();

    @Step("Create web driver")
    public WebDriver getDriver() {
        if (Objects.isNull(driver)) {
            log.info("Create new driver");
            createDriver();
        }
        return driver;
    }
}
