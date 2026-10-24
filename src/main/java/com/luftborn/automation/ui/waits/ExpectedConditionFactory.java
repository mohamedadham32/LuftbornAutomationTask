package com.luftborn.automation.ui.waits;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.Objects;

/** Reusable application-specific conditions; Selenium's built-ins remain available through BaseWaits. */
public final class ExpectedConditionFactory {
    private ExpectedConditionFactory() {}

    public static ExpectedCondition<Boolean> documentReady() {
        return driver -> "complete".equals(((JavascriptExecutor) Objects.requireNonNull(driver))
                .executeScript("return document.readyState"));
    }

    public static ExpectedCondition<Boolean> textNotEmpty(By locator) {
        return driver -> {
            try { return !Objects.requireNonNull(driver).findElement(locator).getText().trim().isEmpty(); }
            catch (RuntimeException ignored) { return false; }
        };
    }

    public static ExpectedCondition<Boolean> attributeEquals(By locator, String attribute, String expected) {
        return driver -> expected.equals(Objects.requireNonNull(driver).findElement(locator).getAttribute(attribute));
    }

    public static ExpectedCondition<Boolean> elementCountAtLeast(By locator, int count) {
        return driver -> Objects.requireNonNull(driver).findElements(locator).size() >= count;
    }

    public static ExpectedCondition<Boolean> cssValueEquals(By locator, String property, String expected) {
        return driver -> expected.equals(Objects.requireNonNull(driver).findElement(locator).getCssValue(property));
    }

    public static ExpectedCondition<WebElement> refreshedElement(By locator) {
        return driver -> {
            WebElement element = Objects.requireNonNull(driver).findElement(locator);
            return element.isDisplayed() ? element : null;
        };
    }

    public static ExpectedCondition<Boolean> urlChangedFrom(String oldUrl) {
        return driver -> !oldUrl.equals(Objects.requireNonNull(driver).getCurrentUrl());
    }
}
