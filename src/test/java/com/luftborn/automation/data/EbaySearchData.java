package com.luftborn.automation.data;

/** External test-data contract for the eBay vehicle search scenario. */
public record EbaySearchData(
        String searchTerm,
        String filterName,
        String filterOption,
        String shippingCountry,
        String shippingPostalCode) {
}
