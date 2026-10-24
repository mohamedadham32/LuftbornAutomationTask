package com.luftborn.automation.ui.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public final class MouseKeyboardActions {
    private final WebDriver driver;
    public MouseKeyboardActions(WebDriver driver) { this.driver = driver; }
    private Actions actions() { return new Actions(driver); }
    private WebElement element(By locator) { return driver.findElement(locator); }

    public void hover(By locator) { actions().moveToElement(element(locator)).perform(); }
    public void click(By locator) { actions().click(element(locator)).perform(); }
    public void doubleClick(By locator) { actions().doubleClick(element(locator)).perform(); }
    public void contextClick(By locator) { actions().contextClick(element(locator)).perform(); }
    public void clickAndHold(By locator) { actions().clickAndHold(element(locator)).perform(); }
    public void release(By locator) { actions().release(element(locator)).perform(); }
    public void dragAndDrop(By source, By target) { actions().dragAndDrop(element(source), element(target)).perform(); }
    public void dragByOffset(By source, int x, int y) { actions().dragAndDropBy(element(source), x, y).perform(); }
    public void moveByOffset(int x, int y) { actions().moveByOffset(x, y).perform(); }
    public void sendKeys(CharSequence... keys) { actions().sendKeys(keys).perform(); }
    public void sendKeys(By locator, CharSequence... keys) { actions().sendKeys(element(locator), keys).perform(); }
    public void keyDown(Keys key) { actions().keyDown(key).perform(); }
    public void keyUp(Keys key) { actions().keyUp(key).perform(); }
}
