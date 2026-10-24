package com.luftborn.automation.ui.waits;

import com.luftborn.automation.core.config.FrameworkConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public abstract class BaseWaits {
    protected final WebDriver driver;
    private final FrameworkConfig config;

    protected BaseWaits(WebDriver driver) {
        this.driver = driver;
        this.config = FrameworkConfig.getInstance();
    }

    protected WebDriverWait waitDriver() { return waitDriver(config.explicitWait()); }
    protected WebDriverWait waitDriver(Duration timeout) {
        return new WebDriverWait(driver, timeout, config.pollingInterval());
    }
    protected <T> T until(ExpectedCondition<T> condition) { return waitDriver().until(condition); }
    protected <T> T until(ExpectedCondition<T> condition, Duration timeout) { return waitDriver(timeout).until(condition); }
    protected WebElement waitForPresent(By locator) { return until(ExpectedConditions.presenceOfElementLocated(locator)); }
    protected List<WebElement> waitForAllPresent(By locator) { return until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator)); }
    protected WebElement waitForVisible(By locator) { return until(ExpectedConditions.visibilityOfElementLocated(locator)); }
    protected List<WebElement> waitForAllVisible(By locator) { return until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator)); }
    protected WebElement waitForClickable(By locator) { return until(ExpectedConditions.elementToBeClickable(locator)); }
    protected boolean waitForInvisible(By locator) { return until(ExpectedConditions.invisibilityOfElementLocated(locator)); }
    protected boolean waitForStaleness(WebElement element) { return until(ExpectedConditions.stalenessOf(element)); }
    protected boolean waitForSelected(By locator) { return until(ExpectedConditions.elementToBeSelected(locator)); }
    protected boolean waitForText(By locator, String text) { return until(ExpectedConditions.textToBePresentInElementLocated(locator, text)); }
    protected boolean waitForAttribute(By locator, String attribute, String value) { return until(ExpectedConditions.attributeToBe(locator, attribute, value)); }
    protected void waitForFrame(By locator) { until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator)); }
    protected void waitForAlert() { until(ExpectedConditions.alertIsPresent()); }
    protected boolean waitForTitle(String title) { return until(ExpectedConditions.titleIs(title)); }
    protected boolean waitForTitleContaining(String title) { return until(ExpectedConditions.titleContains(title)); }
    protected boolean waitForUrl(String url) { return until(ExpectedConditions.urlToBe(url)); }
    protected boolean waitForUrlContaining(String value) { return until(ExpectedConditions.urlContains(value)); }
    protected boolean waitForWindowCount(int count) { return until(ExpectedConditions.numberOfWindowsToBe(count)); }
    protected boolean waitForDocumentReady() { return until(ExpectedConditionFactory.documentReady()); }

    protected <T> T fluentWait(Duration timeout, Duration polling, Function<WebDriver, T> condition) {
        return new FluentWait<>(driver).withTimeout(timeout).pollingEvery(polling)
                .ignoreAll(List.of(NoSuchElementException.class, StaleElementReferenceException.class)).until(condition);
    }

    protected String waitForNewWindow(Set<String> originalHandles) {
        return waitDriver().until(d -> d.getWindowHandles().stream()
                .filter(handle -> !originalHandles.contains(handle)).findFirst().orElse(null));
    }
}
