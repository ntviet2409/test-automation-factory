# About Selenium Automation Framework
This framework is developed to automate UI web applications with Allure reports of test results

## Tools, Approaches and Technologies
* Core Java
* Maven
* Spring
* TestNG
* Selenium WebDriver
* Page Object Model
* Lombok
* Logging with Slf4j
* Allure Report
* Git

## Framework Structure
For integration tests the folder structure should be similar to this as our spec files are going to utilize multiple page objects for completing a test
```
   src
    ├─ allure-results
    ├─ allure-reports
    ├─ pom.xml
    ├─ testng.xml
    ├─ README.md
    ├─ config.properties
    ├─ main
    │   ├─ java
    │   │   ├─ common
    │   │   ├─ environment
    │   │   │   ├─ DriverManager.java
    │   │   │   ├─ DriverManagerFactory.java
    │   │   │   ├─ DriverDownloadManager.java
    │   │   │   └─ ChromeDriverManager.java
    │   │   ├─ logger
    │   │   ├─ pageObjects
    │   │   │   ├─ LoginPage.java
    │   │   │   └─ OtherPage.java
    │   │   ├─ reports
    │   │   │   └─ AllureSetup.java
    │   │   └─ utilities
    │   │       ├─ Actions.java
    │   │       ├─ Asserts.java
    │   │       ├─ OperatingSystem.java
    │   │       ├─ PropertiesHelper.java
    │   │       ├─ retryLogic
    │   │       │       ├─ RetryAnalyzer.java
    │   │       │       └─ RetryListener.java
    │   │       ├─ TestListener.java
    │   │       ├─ UIHelper.java
    │   │       └─ Waits.java
    │   └─ resources
    │        ├─ selenium
    │        │     └─ chromedriver(.exe)
    │        └─ downloads
    │              └─ zip
    └─ test
        ├─ java
        │    └─ com
        │        ├─ LoginTest.java
        │        └─ ...Test.java
        └─ resources
             └─ en.properties
```
### Framework components
| Component Name              | Description                      |
|:----------------------------|:-----------------------------------|
|pom.xml                      | This is Maven core component which defines all dependencies used in the framework. For more info, refer to the tutorial [POM](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html)|
|allure-results               | Contains artifacts of TestNG generated after running test|
|allure-reports               | Contains HTML report after generating Allure report by command line|
|testng.xml                   | Define the suit level and test cases with properties. For more details, refer to For more info, refer to the tutorial [TestNG.xml](https://testng.org/doc/documentation-main.html)|
|README.md                    | Document framework in details |
|main > java > com            | Contains test helpers, utilities which are used in test scripts|
|common                       | A common folder contains common utilities like datetime helpers for common purposes|
|environment                  | All classes in this folder downloads, initiates web driver by referring to config.properties file|
|config.properties            | Define configuration information and highly customize per the execution purpose|
|logger                       | Have the customized logging information of @Slf4j like logging level, extra information|
|pageObjects                  | Page Object Models which contain both web element and methods used for UI web page|
|reports                      | Contains AllureSetup.java to setup allure-reports, allure-results and init allure report for each execution. Refer to the tutorial: [Allure Setup](https://www.swtestacademy.com/allure-report-testng/)|
|Utilities                    | Have basic utilities for web testing purposes|
|Utilities > Actions.java     | Have basic web interaction methods like click, sendKeys empowerd by Selenium APIs|
|Utilities > Waits.java       | Have web synchronizations with explicit waits to wait for certain web elements to be loaded properly|
|Utilities > Asserts.java     | Assert the certain conditions to be happened to decide if the test pass or fail|
|Utilities > OperatingSystem.java   | Detect the type of used operating system like MacOS, Windows, Linux  |
|Utilities > PropertiesHelper.java  | Load the properties like message, configuration, business data from properties file|
|Utilities > retryLogic.java        | Define the logic of retrying running the failed TCs |
|Utilities > TestListener.java      | Define the listener (considered as the test hook - setup, teardown empowered in TestNG [TestNG Listener](https://testng.org/doc/documentation-main.html)|
|Utilities > UIHelper.java          | Consider as a Base Utils which is inherited by all page objects. This reduces the duplicated code, fasten test development progress|
|Resources > selenium               | unzip the webdriver and grant necessary permission|
|Resources > downloads              | Contains downloaded file of web driver in .zip format|
|test > java > com                  | Contains all test scrips empowered by TestNG|
|LoginTest.class                    | test scrips empowered by TestNG which is executed in the proper annotation. Tutorial: [TestNG Annotation](https://www.simplilearn.com/know-about-testng-annotations-in-selenium-webdriver-article)|

## Setup Test
Clone code by GitTool to get the latest code to local machine from this [Repository Link](https://app-git-vm01.acticall.net/mrath013/innso-test-automation)
```
git clone ssh://john@example.com/path/to/my-project.git 
cd my-project 
# Start working on the project
```
Download Maven dependencies
```
mvn clean install
```
Open IDE - Intellij Community - [Download](https://www.jetbrains.com/idea/download/) to review project and update configuration info in `config.properties` with following content:
```properties
locale=en
webdriverVersion=91.0.4472.101
```
|  Key Name                   | Description                        |
|:----------------------------|:-----------------------------------|
|locale                       | Load languague file to support multiple localisations. Keep default value as 'en'|
|webdriverVersion             | Define chrome driver version corresponding to the current browser version. Refer to [Chrome Driver Download](https://chromedriver.chromium.org/downloads)|

## Write New Test
Navigate to `pageObjects` directory and create page object class as below. This phase defines all actions and test step which will be called by test script level
```java
package com.pageObjects.insoCore;

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

    public LoginPage(WebDriver driver){
        super(driver);
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
}
```
* PageObject class inherits `UIHelper` for usage of actions, waits, asserts utilities.

An example for `clickElement` method in `Actions.java`'
```java
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
```

An example for `waitForVisible` method in `Waits.java`'
```java
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
```

An example for `verifyElementIsVisible` method in `Asserts.java`'
```java
@Step("Verify element is visible: {0}")
public void verifyElementIsVisible(final By locator) {
        log.info("Start verifying visible element");
        WebElement ele = waitUtils.waitForVisible(locator);
        Assert.assertTrue(ele.isDisplayed(), ELEMENT_NOT_VISIBLE);
        }
```

* `By` locator is used to declare the locator as utilities only accept `By` type
* `@Slf4j` is attached for logging purpose. Keep using `log.info()` to add logs in each of step
* `@Step` annotation for allure-report logging. The method inside page object class is called from script test level
* Inject web driver `driver` in the constructor

Compose the test script by navigating to `test > java > com` and create new test class. Refer this one as an example:

```java
package com.tests.insocore;

import com.environment.DriverManagerFactory;
import com.pageObjects.CommonPage;
import com.pageObjects.LoginPage;
import com.utilities.TestListener;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestListener.class)
@Epic("Regression Test")
@Feature("Login")
public class LoginTest {
  public LoginPage loginPageRefactor;
  public CommonPage commonPage;
  private WebDriver driver;

  @BeforeMethod
  public void openWeb() {
    driver = DriverManagerFactory.getDriverManager().getDriver();
    loginPageRefactor = new LoginPage(driver);
    commonPage = new CommonPage(driver);
    commonPage.openWebApplication();
  }

  @AfterMethod
  public void closeChromeBrowser() {
    commonPage.closeWebApplication();
  }

  @Test(description = "Creation of new channel - TC-307")
  public void login() {
    loginPageRefactor.login("", "");
    loginPageRefactor.verifyLoginIsDone();
  }
}

```
Test class includes following components:

|  Key Name                   | Description                        |
|:----------------------------|:-----------------------------------|
| @Listeners                  | TestNG listeners for test hooks. Refer TestListener for more details|
|@Epic                        |Allure annotation for adding epic info|
|@Feature                     |Allure annotation for adding feature info|
|@BeforeMethod                |Test setup runs before each test case|
|@AfterMethod                 |Test teardown runs after each test case|
|@Test                        |Identify the scope of test case|
* Before each run, driver is initiated by `driver = DriverManagerFactory.getDriverManager().getDriver();`
* After each run, driver is quit by `commonPage.closeWebApplication();`
* Test name, steps are called from page object class which is defined above

## Run Test
Execute test by Surefire with TestNG
```
mvn clean test
```
* All test classes declared in testng.xml are executed. The artifact of allure-report, screenshot taken by failed test case are created under `target` directory. We have following various command line options available which can be specified as follows with `mvn` command
  All of them are optional at [link](https://mkyong.com/maven/how-to-run-unit-test-with-maven/)
* When running test on IDE, artifacts are created under `allure-results`

## Generate Report
Allure report is generated as .json file under directory "allure-results" under project root directory. Now run the command:
```
allure generate allure-results --clean -o allure-report
```
* Refer to `allure-report` to get `index.html` and open it by web browser
* On HTML, click test suite level or step level to see the detailed log
* Any fail test case contains the attached screenshot under step level for trouble-shooting purpose

## Git Flow
At a high level, the [Git Development Workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) being followed is:

* Create a branch
* Do your work
* Prepare feature/bug_fix branch (Make sure commit history is clean)
* Raise pull request to merge your branch into the main repo branches

## Naming Convention
We are using default conventions which are suggested by Java development team on top of that we are also using some more configuration parameters to produce high quality code.
Refer to this [Tutorial](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html)
