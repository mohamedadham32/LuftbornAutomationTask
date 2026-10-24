package com.luftborn.automation.support.listeners;

import com.luftborn.automation.core.driver.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

public final class TestListener implements ITestListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOGGER.info("TEST STARTED: {}", result.getMethod().getQualifiedName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("TEST PASSED: {} ({} ms)", result.getMethod().getQualifiedName(),
                result.getEndMillis() - result.getStartMillis());
        Allure.addAttachment("Test outcome", "text/plain", "PASSED");
        attachBrowserState("Final browser screenshot");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.error("TEST FAILED: {}", result.getMethod().getQualifiedName(), result.getThrowable());
        Allure.addAttachment("Test outcome", "text/plain", "FAILED");
        attachBrowserState("Failure screenshot");
        if (result.getThrowable() != null) {
            Allure.addAttachment("Failure", "text/plain", result.getThrowable().toString());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warn("TEST SKIPPED: {}", result.getMethod().getQualifiedName());
        Allure.addAttachment("Test outcome", "text/plain", "SKIPPED");
    }

    private void attachBrowserState(String screenshotName) {
        if (!DriverManager.isStarted()) return;
        byte[] image = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(screenshotName, "image/png", new ByteArrayInputStream(image), ".png");
        Allure.addAttachment("Current URL", "text/plain", DriverManager.getDriver().getCurrentUrl());
    }
}
