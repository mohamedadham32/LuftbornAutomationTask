package com.luftborn.automation.pages;

import com.luftborn.automation.ui.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Page object for eBay search results and the left-side filter panel. */
public final class EbaySearchResultsPage extends BasePage {
    private static final By RESULTS_HEADING = By.id("srp-results-heading");
    private static final By PRODUCT_LINKS = By.cssSelector("#srp-river-results a[href*='/itm/']");
    private static final By TEMPORARY_ERROR = By.xpath(
            "//*[normalize-space()='SORRY' or contains(normalize-space(), 'Something went wrong on our end')]");
    private static final By CATEGORY_FALLBACK = By.xpath("//*[contains(normalize-space(), '0 results found in')]");
    private static final Pattern FIRST_NUMBER = Pattern.compile("([0-9][0-9,]*)");

    public EbaySearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public EbaySearchResultsPage waitUntilLoaded() {
        logStep("Wait for eBay search results to load");
        waitForDocumentReady();
        retryTemporaryEbayError();
        waitForVisible(RESULTS_HEADING);
        return this;
    }

    private void retryTemporaryEbayError() {
        for (int attempt = 1; attempt <= 2 && !elements.findAll(TEMPORARY_ERROR).isEmpty(); attempt++) {
            logStep("eBay returned a temporary error page; refresh search results (attempt " + attempt + ")");
            browser.refresh();
            waitForDocumentReady();
        }
    }

    public boolean hasResultsFor(String searchTerm) {
        logStep("Validate results obtained for: " + searchTerm);
        String heading = text(RESULTS_HEADING).toLowerCase(Locale.ROOT);
        boolean containsSearchWords = Arrays.stream(searchTerm.toLowerCase(Locale.ROOT).split("\\s+"))
                .filter(word -> !word.isBlank())
                .allMatch(heading::contains);
        return containsSearchWords && parseResultCount(heading) > 0 && !elements.findAll(PRODUCT_LINKS).isEmpty();
    }

    public long resultCount() {
        long count = parseResultCount(text(RESULTS_HEADING));
        logStep("OBTAINED SEARCH RESULTS: " + count);
        return count;
    }

    public EbaySearchResultsPage filterBy(String filterName, String option) {
        logStep("Filter search results from the left panel: " + filterName + " -> " + option);
        By heading = exactTextAfterResultsHeading(filterName, "button", "h3", "span");
        if (elements.findAll(heading).isEmpty()) {
            throw new IllegalStateException(missingFilterMessage(filterName));
        }

        javascript.scrollIntoView(heading);
        WebElement filterHeading = elements.find(heading);
        if ("button".equalsIgnoreCase(filterHeading.getTagName())
                && "false".equalsIgnoreCase(filterHeading.getAttribute("aria-expanded"))) {
            filterHeading.click();
        }

        By filterOption = By.xpath("(//*[@id='srp-filters-heading']/following::*[normalize-space()="
                + xpathLiteral(filterName) + "][1]/following::*[(self::a or self::label) and "
                + "(starts-with(normalize-space(), " + xpathLiteral(option) + ")"
                + " or .//*[normalize-space()=" + xpathLiteral(option) + "])])[1]");
        WebElement optionElement = waitForClickable(filterOption);
        WebElement previousResultsHeading = waitForVisible(RESULTS_HEADING);
        String previousUrl = browser.currentUrl();
        optionElement.click();

        until(currentDriver -> resultsWereUpdated(previousResultsHeading, previousUrl));
        waitForDocumentReady();
        waitForVisible(RESULTS_HEADING);
        waitForVisible(appliedFilterIndicator(option));
        logStep("Updated results loaded and selected filter confirmed: " + filterName + " -> " + option);
        return this;
    }

    public boolean isFilterSelected(String option) {
        logStep("Validate applied-filter indicator for: " + option);
        return !elements.findAll(appliedFilterIndicator(option)).isEmpty();
    }

    private boolean resultsWereUpdated(WebElement previousResultsHeading, String previousUrl) {
        if (!driver.getCurrentUrl().equals(previousUrl)) {
            return true;
        }
        try {
            return !previousResultsHeading.isDisplayed();
        } catch (StaleElementReferenceException ignored) {
            return true;
        }
    }

    private By appliedFilterIndicator(String option) {
        return By.xpath("(//*[@id='srp-river-results']//*[self::a or self::button]["
                + "(contains(@aria-label, 'Remove filter') and contains(@aria-label, "
                + xpathLiteral(option) + "))"
                + " or (starts-with(normalize-space(), " + xpathLiteral(option) + ")"
                + " and following::*[@id='results'])])[1]");
    }

    private long parseResultCount(String heading) {
        Matcher matcher = FIRST_NUMBER.matcher(heading);
        if (!matcher.find()) {
            throw new IllegalStateException("Could not extract the result count from heading: " + heading);
        }
        return Long.parseLong(matcher.group(1).replace(",", ""));
    }

    private String missingFilterMessage(String filterName) {
        String message = "The eBay left-side filter '" + filterName + "' is not available for these results.";
        if (!elements.findAll(CATEGORY_FALLBACK).isEmpty()) {
            message += " eBay returned no vehicle-category matches for the active locale and fell back to all categories.";
        }
        return message + " Run from a market where eBay Motors results expose this vehicle aspect.";
    }

    private By exactTextAfterResultsHeading(String value, String... tags) {
        String tagExpression = Arrays.stream(tags)
                .map(tag -> "self::" + tag)
                .reduce((left, right) -> left + " or " + right)
                .orElse("self::*");
        return By.xpath("(//*[@id='srp-filters-heading']/following::*[(" + tagExpression
                + ") and normalize-space()=" + xpathLiteral(value) + "])[1]");
    }

    private String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        return "concat('" + value.replace("'", "',\"'\",'") + "')";
    }
}
