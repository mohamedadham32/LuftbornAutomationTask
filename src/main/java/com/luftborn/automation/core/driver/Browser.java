package com.luftborn.automation.core.driver;
public enum Browser {
    CHROME, FIREFOX, EDGE;
    public static Browser from(String value) {
        try { return valueOf(value.trim().toUpperCase()); }
        catch (RuntimeException e) { throw new IllegalArgumentException("Unsupported browser: " + value, e); }
    }
}
