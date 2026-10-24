package com.luftborn.automation.core.driver;

import com.luftborn.automation.core.config.FrameworkConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DriverFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {}
    public static WebDriver create(FrameworkConfig config) {
        LOGGER.info("Creating {} browser; headless={}", config.browser(), config.headless());
        WebDriver driver = switch (Browser.from(config.browser())) {
            case CHROME -> new ChromeDriver(chromeOptions(config));
            case FIREFOX -> new FirefoxDriver(firefoxOptions(config));
            case EDGE -> new EdgeDriver(edgeOptions(config));
        };
        driver.manage().timeouts().pageLoadTimeout(config.pageLoadTimeout());
        driver.manage().timeouts().scriptTimeout(config.scriptTimeout());
        driver.manage().window().maximize();
        LOGGER.info("Browser session created: {}", driver.getClass().getSimpleName());
        return driver;
    }
    private static ChromeOptions chromeOptions(FrameworkConfig c) { ChromeOptions o = new ChromeOptions(); o.addArguments("--disable-notifications", "--start-maximized"); if (c.headless()) o.addArguments("--headless=new", "--window-size=1920,1080"); return o; }
    private static FirefoxOptions firefoxOptions(FrameworkConfig c) { FirefoxOptions o = new FirefoxOptions(); if (c.headless()) o.addArguments("-headless"); return o; }
    private static EdgeOptions edgeOptions(FrameworkConfig c) { EdgeOptions o = new EdgeOptions(); o.addArguments("--disable-notifications"); if (c.headless()) o.addArguments("--headless=new", "--window-size=1920,1080"); return o; }
}
