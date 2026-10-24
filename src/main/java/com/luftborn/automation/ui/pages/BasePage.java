package com.luftborn.automation.ui.pages;

import com.luftborn.automation.ui.actions.BrowserActions;
import com.luftborn.automation.ui.actions.ElementActions;
import com.luftborn.automation.ui.actions.JavaScriptActions;
import com.luftborn.automation.ui.actions.MouseKeyboardActions;
import com.luftborn.automation.ui.waits.BaseWaits;
import com.luftborn.automation.core.logging.StepLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** All page objects inherit waits and receive focused Selenium action services. */
public abstract class BasePage extends BaseWaits {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final ElementActions elements;
    protected final MouseKeyboardActions actions;
    protected final JavaScriptActions javascript;
    protected final BrowserActions browser;

    protected BasePage(WebDriver driver) {
        super(driver);
        this.elements = new ElementActions(driver);
        this.actions = new MouseKeyboardActions(driver);
        this.javascript = new JavaScriptActions(driver);
        this.browser = new BrowserActions(driver);
    }

    protected void logStep(String description) { StepLogger.log(log, description); }
    protected WebElement ready(By locator) { logStep("Wait for element to become visible: " + locator); return waitForVisible(locator); }
    protected void click(By locator) { logStep("Click element: " + locator); waitForClickable(locator).click(); }
    protected void type(By locator, CharSequence... text) {
        logStep("Type into element: " + locator + " (value omitted)");
        WebElement e = waitForVisible(locator);
        e.clear();
        e.sendKeys(text);
    }
    protected String text(By locator) { logStep("Read text from element: " + locator); return waitForVisible(locator).getText(); }
}
