package com.pageObjects;

import com.utilities.UIHelper;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Slf4j
public class LoginPage extends UIHelper {
    public By usernameField = By.name("username");
    public By passwordField = By.name("password");
    public By loginButton = By.xpath("//button[@class='btn btn-default']");
    public By searchInteractionsMenu = By.xpath("//a[@href='#searchInteractions']");
    public By strInteractionPeriod = By.name("strInteractionPeriod");
    public By applyDateRangeButton = By.xpath("//div[contains(@class,'daterangepicker') and contains(@style,'display')]//*[contains(@class,'btn-success')]");
    public By interactionContactIdentity = By.name("interactionContactIdentity");
    public By searchButton = By.xpath("(//button[@class='btn btn-success btn-block'])[2]");
    public By searchInteractionsTable = By.name("interactionContactIdentity");
    public By toggleUserPanel = By.id("toggle-user-panel");
    public By logoutBtn = By.id("logoutForm");
    public By successAlert = By.cssSelector(".alert.alert-success");
    public By relogin = By.cssSelector("[href='/core/login']");
    public By errorAlert = By.cssSelector(".alert.alert-danger");

    public LoginPage(WebDriver driver){
        super(driver);
    }

    @Step("Logout")
    public void logout() {
        log.info("Logout");
        actionUtils.clickElement(toggleUserPanel);
        actionUtils.clickElement(logoutBtn);
        assertUtils.verifyElementIsVisible(successAlert);
    }

    @Step("Click re-login button")
    public void clickRelogin() {
        log.info("Click re-login button");
        actionUtils.clickElement(relogin);
    }

    @Step("Input correct credentials")
    public void loginCorrectCredentials() {
        log.info("Login with correct credentials");
        login(props.getProperty("valid.username"), props.getProperty("valid.password"));
    }

    @Step("Login with user name {0} and password {1}")
    public void login(String name, String password) {
        log.info("Login with correct credentials");
        actionUtils.sendKeys(usernameField, name);
        actionUtils.sendKeys(passwordField, password);
        actionUtils.clickElement(loginButton);
    }

    @Step("Verify login is done")
    public void verifyLoginIsDone() {
        assertUtils.verifyElementIsVisible(searchInteractionsMenu);
    }

    @Step("Verify login is failed")
    public void verifyLoginIsFailed() {
        log.info("Verify login is failed with incorrect credentials");
        assertUtils.verifyElementIsVisible(errorAlert);
    }

    @Step("Search ticket interaction by today")
    public void searchTicketInteractionByToday(String text) {
        log.info("Search ticket interaction by today");
        actionUtils.clickElement(searchInteractionsMenu);
        actionUtils.clickElement(strInteractionPeriod);
        actionUtils.clickElement(applyDateRangeButton);
        actionUtils.sendKeys(interactionContactIdentity, text);
        actionUtils.clickElement(searchButton);
    }
}
