package com.luftborn.automation.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Locale;
import java.util.Properties;

public final class FrameworkConfig {
    private static final FrameworkConfig INSTANCE = new FrameworkConfig();
    private final Properties properties = new Properties();
    private FrameworkConfig() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("config/framework.properties")) {
            if (stream == null) throw new IllegalStateException("Missing classpath resource: config/framework.properties");
            properties.load(stream);
        } catch (IOException e) { throw new IllegalStateException("Could not load config/framework.properties", e); }
    }
    public static FrameworkConfig getInstance() { return INSTANCE; }
    public String get(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Missing configuration value: " + key);
        return value.trim();
    }
    public String baseUrl() { return get("base.url"); }
    public String browser() { return get("browser").toLowerCase(Locale.ROOT); }
    public boolean headless() { return Boolean.parseBoolean(get("headless")); }
    public Duration explicitWait() { return Duration.ofSeconds(Long.parseLong(get("explicit.wait.seconds"))); }
    public Duration pageLoadTimeout() { return Duration.ofSeconds(Long.parseLong(get("page.load.timeout.seconds"))); }
    public Duration scriptTimeout() { return Duration.ofSeconds(Long.parseLong(get("script.timeout.seconds"))); }
    public Duration pollingInterval() { return Duration.ofMillis(Long.parseLong(get("polling.milliseconds"))); }
}
