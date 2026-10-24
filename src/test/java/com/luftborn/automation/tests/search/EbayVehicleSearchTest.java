package com.luftborn.automation.tests.search;

import com.luftborn.automation.data.EbaySearchData;
import com.luftborn.automation.data.json.JsonDataReader;
import com.luftborn.automation.pages.EbayHomePage;
import com.luftborn.automation.pages.EbaySearchResultsPage;
import com.luftborn.automation.support.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("eBay Web Automation")
@Feature("Vehicle search and filtering")
public final class EbayVehicleSearchTest extends BaseTest {
    private static final String DATA_FILE = "data/ebay-search.json";

    @Test(description = "Search for a Mazda MX-5 and filter manual transmissions")
    @Description("Validates the eBay home page and search results, logs the result count, then applies Transmission -> Manual from the left filter panel.")
    public void shouldSearchAndFilterManualMazdaMx5Vehicles() {
        EbaySearchData data = JsonDataReader.read(DATA_FILE, EbaySearchData.class);
        Allure.parameter("search term", data.searchTerm());
        Allure.parameter("filter", data.filterName() + " -> " + data.filterOption());

        EbayHomePage homePage = new EbayHomePage(driver()).waitUntilLoaded();
        Allure.step("Validate eBay main page", () ->
                Assert.assertTrue(homePage.isLoaded(), "The eBay main page did not load correctly"));
        homePage.setShippingLocation(data.shippingCountry(), data.shippingPostalCode());

        EbaySearchResultsPage resultsPage = homePage.searchFor(data.searchTerm());
        Allure.step("Validate search results", () ->
                Assert.assertTrue(resultsPage.hasResultsFor(data.searchTerm()),
                        "No relevant eBay results were obtained for " + data.searchTerm()));

        long resultCount = resultsPage.resultCount();
        Allure.addAttachment("Obtained search result count", "text/plain", Long.toString(resultCount));
        Assert.assertTrue(resultCount > 0, "The eBay search result count must be greater than zero");

        resultsPage.filterBy(data.filterName(), data.filterOption());
        Allure.step("Validate selected transmission filter", () ->
                Assert.assertTrue(resultsPage.isFilterSelected(data.filterOption()),
                        "The eBay applied-filter indicator does not show " + data.filterOption()
                                + " as selected"));

    }
}
