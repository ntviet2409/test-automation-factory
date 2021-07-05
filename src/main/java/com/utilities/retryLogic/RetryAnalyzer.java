package com.utilities.retryLogic;

import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {
    int retryCount = 0;
    int maxRetryCount = 3;

    /* Below method returns 'true' if the test method has to be retried else 'false'
    and it takes the 'Result' as parameter of the test method that just ran */

    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            log.info("Retrying " + result.getName() + " test with status "
                    + getResultStatusName(result.getStatus()) + " for the " + (retryCount + 1) + " time(s).");
            retryCount++;
            return true;
        }
        return false;
    }

    public String getResultStatusName(int status) {
        String resultName = null;
        if (status == 1)
            resultName = "SUCCESS";
        if (status == 2)
            resultName = "FAILURE";
        if (status == 3)
            resultName = "SKIP";
        return resultName;
    }
}
