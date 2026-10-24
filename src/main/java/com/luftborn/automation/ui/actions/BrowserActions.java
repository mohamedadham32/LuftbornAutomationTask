package com.luftborn.automation.ui.actions;

import com.luftborn.automation.core.logging.StepLogger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.Set;

public final class BrowserActions {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserActions.class);
    private final WebDriver driver;
    public BrowserActions(WebDriver driver) { this.driver = driver; }

    public void open(String url) { StepLogger.log(LOGGER, "Open URL: " + url); driver.get(url); }
    public String title() { return driver.getTitle(); }
    public String currentUrl() { return driver.getCurrentUrl(); }
    public String pageSource() { return driver.getPageSource(); }
    public void back() { StepLogger.log(LOGGER, "Navigate back"); driver.navigate().back(); }
    public void forward() { StepLogger.log(LOGGER, "Navigate forward"); driver.navigate().forward(); }
    public void refresh() { StepLogger.log(LOGGER, "Refresh current page"); driver.navigate().refresh(); }
    public void navigateTo(String url) { StepLogger.log(LOGGER, "Navigate to URL: " + url); driver.navigate().to(url); }
    public String windowHandle() { return driver.getWindowHandle(); }
    public Set<String> windowHandles() { return driver.getWindowHandles(); }
    public void switchToWindow(String handle) { StepLogger.log(LOGGER, "Switch to browser window"); driver.switchTo().window(handle); }
    public void switchToFrame(int index) { StepLogger.log(LOGGER, "Switch to frame index " + index); driver.switchTo().frame(index); }
    public void switchToFrame(String nameOrId) { StepLogger.log(LOGGER, "Switch to frame: " + nameOrId); driver.switchTo().frame(nameOrId); }
    public void switchToFrame(WebElement frame) { StepLogger.log(LOGGER, "Switch to frame element"); driver.switchTo().frame(frame); }
    public void switchToParentFrame() { StepLogger.log(LOGGER, "Switch to parent frame"); driver.switchTo().parentFrame(); }
    public void switchToDefaultContent() { StepLogger.log(LOGGER, "Switch to default content"); driver.switchTo().defaultContent(); }
    public Alert alert() { return driver.switchTo().alert(); }
    public void acceptAlert() { StepLogger.log(LOGGER, "Accept browser alert"); alert().accept(); }
    public void dismissAlert() { StepLogger.log(LOGGER, "Dismiss browser alert"); alert().dismiss(); }
    public void typeInAlert(String text) { StepLogger.log(LOGGER, "Type into browser alert (value omitted)"); alert().sendKeys(text); }
    public String alertText() { return alert().getText(); }
    public void addCookie(Cookie cookie) { driver.manage().addCookie(cookie); }
    public Cookie cookie(String name) { return driver.manage().getCookieNamed(name); }
    public Set<Cookie> cookies() { return driver.manage().getCookies(); }
    public void deleteCookie(String name) { driver.manage().deleteCookieNamed(name); }
    public void deleteAllCookies() { driver.manage().deleteAllCookies(); }
    public byte[] screenshot() { return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES); }
    public Path screenshot(Path destination) {
        Path source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
        try {
            Path absolute = destination.toAbsolutePath();
            if (absolute.getParent() != null) Files.createDirectories(absolute.getParent());
            return Files.copy(source, absolute, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save screenshot to " + destination, e);
        }
    }
    public void closeWindow() { StepLogger.log(LOGGER, "Close current browser window"); driver.close(); }
}
