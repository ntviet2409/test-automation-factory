package com.utilities;

import com.environment.DriverManagerFactory;
import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.Set;

@Slf4j
public class TestListener implements ITestListener {
	private static final String TEST_START = "\n-------------------------------------------------------------------\n" +
			" _____ _____ ____ _____   ____ _____  _    ____ _____ _____ ____  \n" +
			"|_   _| ____/ ___|_   _| / ___|_   _|/ \\  |  _ \\_   _| ____|  _ \\ \n" +
			"  | | |  _| \\___ \\ | |   \\___ \\ | | / _ \\ | |_) || | |  _| | | | |\n" +
			"  | | | |___ ___) || |    ___) || |/ ___ \\|  _ < | | | |___| |_| |\n" +
			"  |_| |_____|____/ |_|   |____/ |_/_/   \\_\\_| \\_\\|_| |_____|____/ \n" +
			"\n";
	private static final String TEST_PASS = "\n" +
			"        __    _____ _____ ____ _____   ____   _    ____  ____  _____ ____  \n" +
			"  _     \\ \\  |_   _| ____/ ___|_   _| |  _ \\ / \\  / ___|/ ___|| ____|  _ \\ \n" +
			" (_)_____| |   | | |  _| \\___ \\ | |   | |_) / _ \\ \\___ \\\\___ \\|  _| | | | |\n" +
			"  _|_____| |   | | | |___ ___) || |   |  __/ ___ \\ ___) |___) | |___| |_| |\n" +
			" (_)     | |   |_| |_____|____/ |_|   |_| /_/   \\_\\____/|____/|_____|____/ \n" +
			"        /_/                                                                \n" +
			"\n";
	private static final String TEST_FAIL = "\n           __  _____ _____ ____ _____   _____ _    ___ _     _____ ____  \n" +
			"  _       / / |_   _| ____/ ___|_   _| |  ___/ \\  |_ _| |   | ____|  _ \\ \n" +
			" (_)_____| |    | | |  _| \\___ \\ | |   | |_ / _ \\  | || |   |  _| | | | |\n" +
			"  _|_____| |    | | | |___ ___) || |   |  _/ ___ \\ | || |___| |___| |_| |\n" +
			" (_)     | |    |_| |_____|____/ |_|   |_|/_/   \\_\\___|_____|_____|____/ \n" +
			"          \\_\\                                                            \n" +
			"--------------------------------------------------------------------------";

	@Attachment(value = "Failed Screen shoot", type = "image/png")
	public byte[] saveScreenshot(WebDriver driver){
		log.info("Taking screenshot");
		return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
	}

	@Override
	public void onFinish(ITestContext context) {
		Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
		for (ITestResult temp : failedTests) {
			ITestNGMethod method = temp.getMethod();
			if (context.getFailedTests().getResults(method).size() > 1) {
				failedTests.remove(temp);
			} else {
				if (context.getPassedTests().getResults(method).size() > 0) {
					failedTests.remove(temp);
				}
			}
		}
	}

	public void onTestStart(ITestResult result) {
		log.info(TEST_START + "New Test Started: " + result.getName());
	}

	public void onTestSuccess(ITestResult result) {
		log.info(TEST_PASS + "Successfully Finished: " + result.getName());
	}

	public void onTestFailure(ITestResult result) {
		saveScreenshot(UIHelper.threadLocalDriver.get());
		log.info(TEST_FAIL);
	}

	public void onTestSkipped(ITestResult result) {}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

	public void onStart(ITestContext context) {
		if (OperatingSystem.getCurrentOS().equals(OperatingSystem.WINDOWS)) {
			try {
				log.info("Kill processes from the command line");
				String cmd = "taskkill /F /IM chromedriver.exe /T";
				Runtime.getRuntime().exec(cmd);
				log.info("Done");
			} catch (Exception e) {
				log.info("Unable to kill processes");
			}
		}
	}
}  


