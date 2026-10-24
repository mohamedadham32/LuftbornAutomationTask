package com.luftborn.automation.core.driver;
import com.luftborn.automation.core.config.FrameworkConfig;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public final class DriverManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private DriverManager() {}
    public static void start() {
        if (DRIVER.get() != null) throw new IllegalStateException("Driver already active");
        LOGGER.info("Starting WebDriver on thread {}", Thread.currentThread().getName());
        DRIVER.set(DriverFactory.create(FrameworkConfig.getInstance()));
    }
    public static WebDriver getDriver() { WebDriver d = DRIVER.get(); if (d == null) throw new IllegalStateException("Driver is not initialized"); return d; }
    public static void quit() {
        WebDriver d = DRIVER.get();
        try {
            if (d != null) {
                LOGGER.info("Closing WebDriver on thread {}", Thread.currentThread().getName());
                d.quit();
            }
        } catch (RuntimeException exception) {
            LOGGER.warn("WebDriver did not close cleanly", exception);
        } finally {
            DRIVER.remove();
        }
    }
    public static boolean isStarted() { return DRIVER.get() != null; }
}
