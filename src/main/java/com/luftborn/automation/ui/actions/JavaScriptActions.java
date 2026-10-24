package com.luftborn.automation.ui.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class JavaScriptActions {
    private final WebDriver driver;
    private final JavascriptExecutor js;
    public JavaScriptActions(WebDriver driver) { this.driver = driver; this.js = (JavascriptExecutor) driver; }
    private WebElement element(By locator) { return driver.findElement(locator); }

    public Object execute(String script, Object... arguments) { return js.executeScript(script, arguments); }
    public Object executeAsync(String script, Object... arguments) { return js.executeAsyncScript(script, arguments); }
    public void click(By locator) { execute("arguments[0].click();", element(locator)); }
    public void setValue(By locator, String value) { execute("arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('input',{bubbles:true}));", element(locator), value); }
    public void scrollIntoView(By locator) { execute("arguments[0].scrollIntoView({block:'center',inline:'nearest'});", element(locator)); }
    public void scrollBy(int x, int y) { execute("window.scrollBy(arguments[0],arguments[1]);", x, y); }
    public void scrollToTop() { execute("window.scrollTo(0,0);"); }
    public void scrollToBottom() { execute("window.scrollTo(0,document.body.scrollHeight);"); }
    public void highlight(By locator) { execute("arguments[0].style.outline='3px solid #ff3366';", element(locator)); }
    public String documentState() { return String.valueOf(execute("return document.readyState;")); }
}
