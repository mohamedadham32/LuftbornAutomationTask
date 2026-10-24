package com.luftborn.automation.support;

import com.luftborn.automation.core.config.FrameworkConfig;
import com.luftborn.automation.core.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeMethod(alwaysRun = true)
    public void openBrowser() {
        log.info("Starting test browser for {}", getClass().getSimpleName());
        DriverManager.start();
        driver().get(FrameworkConfig.getInstance().baseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        log.info("Finishing test browser for {}", getClass().getSimpleName());
        DriverManager.quit();
    }

    protected WebDriver driver() { return DriverManager.getDriver(); }
}
