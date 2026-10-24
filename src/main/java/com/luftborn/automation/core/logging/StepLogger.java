package com.luftborn.automation.core.logging;

import org.slf4j.Logger;

/** Produces consistent test-step messages without sequence numbers. */
public final class StepLogger {
    private StepLogger() {}

    public static void log(Logger logger, String description) {
        logger.info("STEP | {}", description);
    }
}
