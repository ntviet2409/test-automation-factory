package com.utilities;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

@Slf4j
public class Asserts {
    protected Waits waitUtils;
    protected WebDriver driver;

    protected Asserts(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new Waits(driver);
    }

    @Step("Verify element is visible: {0}")
    public void verifyElementIsVisible(final By locator) {
        log.info("Start verifying visible element");
        WebElement ele = waitUtils.waitForVisible(locator);
        Assert.assertTrue(ele.isDisplayed(), "");
    }

    @Step("Verify element is not visible: {0}")
    public void verifyElementIsNotVisible(By locator) {
        boolean isInvisibleStatus = waitUtils.tryToWaitForInvisible(locator);
        Assert.assertTrue(isInvisibleStatus);
    }
}
