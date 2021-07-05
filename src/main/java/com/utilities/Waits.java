package com.utilities;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

@Slf4j
public class Waits {
    private static final int TIMEOUT_IN_SECOND = 10;
    private WebDriverWait wait;
    private WebDriver driver;

    public Waits(WebDriver driver) {
        this.driver = driver;
        this.wait = createWaitInstance(TIMEOUT_IN_SECOND);
    }

    private WebDriverWait createWaitInstance(final long timeOutInSeconds) {
        if (this.wait == null) {
            this.wait = new WebDriverWait(this.driver, timeOutInSeconds);
        }
        return this.wait;
    }

    @Step("Wait for element to be visible: {0}")
    public WebElement waitForVisible(final By locator) {
        WebElement ele = null;
        try {
            waitForJSAndJQueryToLoad();
            log.info("Start waiting for visible element: " + locator.toString());
            ele = this.wait
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(ElementClickInterceptedException.class)
                    .ignoring(ElementNotInteractableException.class)
                    .ignoring(ElementClickInterceptedException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));

        } catch (Exception e) {
            log.info("Element is not visible. Therefore marking this test case as failed!");
            Assert.fail(e.getMessage());
        }
        return ele;
    }

    @Step("Wait for element to be clickable: {0}")
    public WebElement waitForClickable(final By locator) {
        WebElement ele = null;
        try {
            waitForJSAndJQueryToLoad();
            log.info("Start waiting for clickable element: " + locator.toString());
            ele = this.wait
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(ElementClickInterceptedException.class)
                    .until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            log.info("Element is not clickable. Therefore marking this test case as failed!");
            Assert.fail(e.getMessage());
        }
        return ele;
    }

    @Step("Try to wait for element to be visible: {0}")
    public WebElement tryToWaitForVisible(final By locator) {
        WebElement ele = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, 1);
            waitForJSAndJQueryToLoad();
            log.info("Start waiting for visible element: " + locator.toString());
            ele = wait
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(ElementClickInterceptedException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return ele;
    }

    @Step("Try to wait for element to be invisible: {0}")
    public boolean tryToWaitForInvisible(final By locator) {
        boolean isDisplayed = false;
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            waitForJSAndJQueryToLoad();
            log.info("Start waiting for invisible element: " + locator.toString());
            isDisplayed = wait
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(ElementClickInterceptedException.class)
                    .until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return isDisplayed;
    }

    @Step("Try to wait for element to be presented: {0}")
    public WebElement tryToWaitForPresented(final By locator) {
        WebElement ele = null;
        try {
            waitForJSAndJQueryToLoad();
            log.info("Start waiting for presented element: " + locator.toString());
            ele = this.wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return ele;
    }

    @Step("Static wait in {0} ms")
    public void waitStatic(int timeoutInMils) {
        try {
            Thread.sleep(timeoutInMils);
        } catch (Exception e) {}
    }

    public boolean waitForJSAndJQueryToLoad() {
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long)((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
                }
                catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };

        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };
        wait.until(driver -> ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"));
        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }
}
