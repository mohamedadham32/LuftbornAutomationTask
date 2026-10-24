package com.luftborn.automation.pages;

import com.luftborn.automation.ui.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

/** Page object for the public eBay home page. */
public final class EbayHomePage extends BasePage {
    private static final By HOME_LOGO = By.cssSelector("a[aria-label='eBay Home'], #gh-logo");
    private static final By SEARCH_INPUT = By.id("gh-ac");
    private static final By SHIPPING_LOCATION_BUTTON = By.xpath(
            "(//button[contains(normalize-space(), ',') or contains(@aria-label, 'Ship to') "
                    + "or contains(@aria-label, 'shipping')])[1]");
    private static final By ACTIVE_DIALOG = By.cssSelector("[role='dialog'][aria-hidden='false']");
    private static final By SHIPPING_DIALOG_FRAME = By.cssSelector(
            "[role='dialog'][aria-hidden='false'] iframe");
    private static final By COUNTRY = By.id("country");
    private static final By POSTAL_CODE = By.id("postalCode");
    private static final By CONFIRM_LOCATION = By.xpath("//button[normalize-space()='Confirm']");
    private static final By DIALOG_CLOSE = By.cssSelector(
            "[role='dialog'][aria-hidden='false'] button[aria-label*='Close'], "
                    + "[role='dialog'][aria-hidden='false'] button.lightbox-dialog__close");

    public EbayHomePage(WebDriver driver) {
        super(driver);
    }

    public EbayHomePage waitUntilLoaded() {
        logStep("Wait for the eBay home page to load");
        waitForDocumentReady();
        waitForVisible(SEARCH_INPUT);
        dismissOptionalDialog();
        return this;
    }

    public boolean isLoaded() {
        logStep("Validate that the eBay main page is displayed");
        return browser.title().toLowerCase().contains("ebay")
                && elements.exists(HOME_LOGO)
                && elements.displayed(SEARCH_INPUT);
    }

    public EbayHomePage setShippingLocation(String country, String postalCode) {
        if (country == null || country.isBlank() || postalCode == null || postalCode.isBlank()) {
            return this;
        }

        logStep("Set eBay test shipping location to " + country + " " + postalCode);
        if (!waitForAutomaticShippingDialog()) {
            click(SHIPPING_LOCATION_BUTTON);
            waitForVisible(ACTIVE_DIALOG);
        }

        waitForFrame(SHIPPING_DIALOG_FRAME);
        try {
            waitForVisible(COUNTRY);
            elements.selectByText(COUNTRY, country);
            elements.clearAndType(POSTAL_CODE, postalCode);
            click(CONFIRM_LOCATION);
        } finally {
            browser.switchToDefaultContent();
        }
        waitForInvisible(ACTIVE_DIALOG);
        waitForVisible(SEARCH_INPUT);
        return this;
    }

    private boolean waitForAutomaticShippingDialog() {
        try {
            until(ExpectedConditions.visibilityOfElementLocated(ACTIVE_DIALOG), Duration.ofSeconds(3));
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
    }

    public EbaySearchResultsPage searchFor(String searchTerm) {
        logStep("Search eBay for: " + searchTerm);
        type(SEARCH_INPUT, searchTerm);
        dismissOptionalDialog();
        logStep("Submit the eBay search");
        elements.submit(SEARCH_INPUT);
        return new EbaySearchResultsPage(driver).waitUntilLoaded();
    }

    private void dismissOptionalDialog() {
        if (elements.findAll(ACTIVE_DIALOG).isEmpty() || elements.findAll(DIALOG_CLOSE).isEmpty()) {
            return;
        }
        logStep("Dismiss eBay's optional shipping-location dialog");
        elements.find(DIALOG_CLOSE).click();
        waitForInvisible(ACTIVE_DIALOG);
    }
}
