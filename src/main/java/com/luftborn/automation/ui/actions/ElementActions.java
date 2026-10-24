package com.luftborn.automation.ui.actions;

import com.luftborn.automation.core.logging.StepLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ElementActions {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElementActions.class);
    private final WebDriver driver;
    public ElementActions(WebDriver driver) { this.driver = driver; }

    public WebElement find(By locator) { return driver.findElement(locator); }
    public List<WebElement> findAll(By locator) { return driver.findElements(locator); }
    public void click(By locator) { StepLogger.log(LOGGER, "Click element: " + locator); find(locator).click(); }
    public void type(By locator, CharSequence... value) { StepLogger.log(LOGGER, "Type into element: " + locator + " (value omitted)"); find(locator).sendKeys(value); }
    public void clear(By locator) { StepLogger.log(LOGGER, "Clear element: " + locator); find(locator).clear(); }
    public void clearAndType(By locator, CharSequence... value) { StepLogger.log(LOGGER, "Clear and type into element: " + locator + " (value omitted)"); WebElement e = find(locator); e.clear(); e.sendKeys(value); }
    public void submit(By locator) { StepLogger.log(LOGGER, "Submit element: " + locator); find(locator).submit(); }
    public String text(By locator) { return find(locator).getText(); }
    public String tagName(By locator) { return find(locator).getTagName(); }
    public String attribute(By locator, String name) { return find(locator).getAttribute(name); }
    public String domAttribute(By locator, String name) { return find(locator).getDomAttribute(name); }
    public String property(By locator, String name) { return find(locator).getDomProperty(name); }
    public String css(By locator, String property) { return find(locator).getCssValue(property); }
    public boolean displayed(By locator) { return find(locator).isDisplayed(); }
    public boolean enabled(By locator) { return find(locator).isEnabled(); }
    public boolean selected(By locator) { return find(locator).isSelected(); }
    public boolean exists(By locator) { return !findAll(locator).isEmpty(); }
    public void selectByText(By locator, String text) { StepLogger.log(LOGGER, "Select option by visible text from: " + locator); new Select(find(locator)).selectByVisibleText(text); }
    public void selectByValue(By locator, String value) { StepLogger.log(LOGGER, "Select option by value from: " + locator); new Select(find(locator)).selectByValue(value); }
    public void selectByIndex(By locator, int index) { StepLogger.log(LOGGER, "Select option index " + index + " from: " + locator); new Select(find(locator)).selectByIndex(index); }
    public void deselectAll(By locator) { StepLogger.log(LOGGER, "Deselect all options from: " + locator); new Select(find(locator)).deselectAll(); }
    public String selectedOption(By locator) { return new Select(find(locator)).getFirstSelectedOption().getText(); }
    public List<String> options(By locator) { return new Select(find(locator)).getOptions().stream().map(WebElement::getText).toList(); }
}
