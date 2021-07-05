package com.pageObjects;

import com.utilities.UIHelper;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public class CommonPage extends UIHelper {
    public CommonPage(WebDriver driver){
        super(driver);
    }

    @Step("Open web application")
    public void openWebApplication() {
        log.info("Open web application");
        getUrl(props.getProperty("base.url"));
    }

    @Step("Close web application")
    public void closeWebApplication() {
        log.info("Close web application");
        closeDriverInstance();
    }


}
