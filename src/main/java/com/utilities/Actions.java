package com.utilities;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.testng.Assert;

import java.util.Objects;

@Slf4j
public class Actions {
    private Waits waitUtils;
    private WebDriver driver;
    public static final int FIVE_SECONDS = 5000;
    public static final int ONE_SECONDS = 1000;

    protected Actions(WebDriver driver, Waits waitUtils) {
        this.driver = driver;
        this.waitUtils = waitUtils;
    }

    @Step("Click element: {0}")
    public void clickElement(By locator) {
        WebElement ele;
        try {
            this.waitUtils.waitForVisible(locator);
            ele = this.waitUtils.waitForClickable(locator);
            log.info("Start clicking element");
            ele.click();
            this.waitUtils.waitForJSAndJQueryToLoad();
            log.info("Done");
        } catch (Exception e) {
            log.error("Element can't be clicked");
            Assert.fail(e.getMessage());
        }
    }

    @Step("Send text: {1} into field: {0}")
    public void sendKeys(By locator, String text) {
        WebElement ele;
        try {
            ele = this.waitUtils.waitForVisible(locator);
            log.info("Clear text field");
            ele.clear();
            log.info(String.format("Send text: %s", text));
            ele.sendKeys(text);
            this.waitUtils.waitForJSAndJQueryToLoad();
            log.info("Done");
        } catch (Exception e) {
            log.error("Element can't be interacted");
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Step("Get text of element: {locator}")
    public String getElementText(By locator) {
        String text;
        log.info("Get text of element: " + locator);
        WebElement ele = waitUtils.waitForVisible(locator);
        text = ele.getText().trim();
        log.info("Text is: " + text);
        return text;
    }

    @Step("Click element: {0} and wait: {1}")
    public void clickElementAndWait(By locator, int timeoutInMils) {
        clickElement(locator);
        waitUtils.waitStatic(timeoutInMils);
    }

    @Step("Send text to field: {0} and wait: {1}")
    public void sendKeysAndWait(By locator, String text, int timeoutInMils) {
        sendKeys(locator, text);
        waitUtils.waitStatic(timeoutInMils);
    }

    @Step("Send special keyboard {1} to field: {0} and wait in: {2}")
    public void sendKeyboardsAndWait(By locator, CharSequence text, int timeoutInMils) {
        WebElement ele = waitUtils.waitForVisible(locator);
        log.info("Send char sequence to the text field");
        ele.sendKeys(text);
        waitUtils.waitStatic(timeoutInMils);
    }

    @Step("Double click on element: {0}")
    public void doubleClick(By locator) {
        WebElement ele = waitUtils.waitForVisible(locator);
        org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(driver);
        log.info("Double click on element");
        action.moveToElement(ele).doubleClick().perform();
    }

    @Step("Get attribute of element: {0}")
    public String getElementAttribute(By locator, String attribute) {
        waitUtils.waitStatic(1000);
        WebElement ele = waitUtils.tryToWaitForPresented(locator);
        String value = "";
        if (!Objects.isNull(ele)) {
            value = ele.getAttribute(attribute).trim();
        }
        log.info("Attribute value is: " + value);
        return value;
    }

    @Step("Click by Java Script on element: {0}")
    public void clickUsingJavaScript(By locator) {
        try {
            waitUtils.waitStatic(ONE_SECONDS);
            final JavascriptExecutor js = (JavascriptExecutor) driver;
            log.info("Click by Javascript on element: " + locator);
            js.executeScript("arguments[0].click();", driver.findElement(locator));
        } catch (final Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Step("Check Windows by title: {0}")
    public boolean checkWindowsByTitle(String title) {
        boolean isWindowsDisplayed = false;
        String currentWindowsHandler = driver.getWindowHandle();
        try {
            waitUtils.waitStatic(1000);
            for(String windowHandle : driver.getWindowHandles()) {
                driver.switchTo().window(windowHandle);
                String actualTitle = driver.getTitle();
                log.info(String.format("Windows with title: '%s' is now available", actualTitle));
                if (actualTitle.equals(title)) {
                    isWindowsDisplayed = true;
                    break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        log.info("Switch back to the current windows");
        driver.switchTo().window(currentWindowsHandler);
        return isWindowsDisplayed;
    }

    @Step("Check Windows contains element: {0}")
    public boolean checkWindowsContainElement(By locator) {
        boolean isWindowsDisplayed = false;
        String currentWindowsHandler = driver.getWindowHandle();
        try {
            waitUtils.waitStatic(1000);
            for(String windowHandle : driver.getWindowHandles()) {
                driver.switchTo().window(windowHandle);
                String actualTitle = driver.getTitle();
                WebElement ele = waitUtils.tryToWaitForVisible(locator);
                if (Objects.nonNull(ele)) {
                    log.info("Element is detected. Windows is found!");
                    isWindowsDisplayed = true;
                    break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        log.info("Switch back to the current windows");
        driver.switchTo().window(currentWindowsHandler);
        return isWindowsDisplayed;
    }
}
