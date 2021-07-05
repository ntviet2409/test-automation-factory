package com.utilities;

import com.reports.AllureSetup;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.Properties;

@Slf4j
public class UIHelper {
    protected Waits waitUtils;
    protected Actions actionUtils;
    protected Asserts assertUtils;
    protected WebDriver driver;
    protected Properties props;
    private static boolean isHooked;
    public static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<WebDriver>();

    protected UIHelper(WebDriver driver) {
        this.driver = driver;
        this.props = PropertiesHelper.loadLocalePropertiesFile();
        threadLocalDriver.set(this.driver);
        this.waitUtils = new Waits(driver);
        this.actionUtils = new Actions(driver, waitUtils);
        this.assertUtils = new Asserts(driver);
        if (!isHooked) {
            log.info("Start setting up allure report...");
            AllureSetup.prepareAllureResultsDir();
            isHooked = true;
        }
    }

    @Step("Open url: {0}")
    protected void getUrl(String url) {
        log.info("Get url: " + url);
        driver.get(url);
    }

    @Step("Close driver")
    protected void closeDriverInstance() {
        threadLocalDriver.get().quit();
    }
}
