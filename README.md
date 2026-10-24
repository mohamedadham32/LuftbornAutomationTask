# eBay Selenium Test Automation

A Maven-based Java UI automation framework implementing the requested eBay vehicle-search flow with Selenium, TestNG, Page Object Model, Allure, external JSON/Excel data support, explicit/fluent waits, reusable browser actions, and step logging.

## Automated flow

1. Open `https://www.ebay.com/`.
2. Validate the eBay main page.
3. Search for the externally configured term (`mazda mx-5`).
4. Validate that relevant results were obtained.
5. Extract and log the displayed result count.
6. Use the left filter panel to select `Transmission -> Manual`.
7. Wait for the result set to refresh, then assert that the `Manual` applied-filter indicator is visible.

The search term, filter values, and neutral US test-market location are stored in `src/test/resources/data/ebay-search.json`; none of this scenario's test data is hardcoded in the test class. The test sets that market through eBay's shipping-location UI before searching so the vehicle inventory and Transmission facet are repeatable outside the US.

## Design

- `DriverManager` owns one browser per test thread, so TestNG parallel execution is safe.
- `DriverFactory` creates Chrome, Firefox, or Edge through Selenium Manager (no driver binaries in the repository).
- `BasePage` inherits `BaseWaits` and composes focused element, browser, JavaScript, mouse, and keyboard services.
- `ExpectedConditionFactory` contains reusable custom conditions; `BaseWaits` exposes Selenium's standard conditions.
- `JsonDataReader`, `JsonDataWriter`, `ExcelDataReader`, and `ExcelDataWriter` keep test-data concerns outside page objects and tests.
- Application page objects are reusable production-side components under `src/main/java/com/luftborn/automation/pages`.
- `TestListener` records the final `PASSED`, `FAILED`, or `SKIPPED` outcome in Allure. Passed and failed UI tests include the final browser screenshot and URL; failures also include the assertion/exception details.
- SLF4J with Logback records business and Selenium steps, framework events, and test lifecycle events to the console and `target/logs/automation.log`; sensitive typed values are omitted.

## Project structure

```text
src
├── main
│   ├── java/com/luftborn/automation
│   │   ├── core
│   │   │   ├── config       # Typed configuration
│   │   │   └── driver       # Browser creation and thread-safe lifecycle
│   │   ├── data
│   │   │   ├── excel        # Excel adapters
│   │   │   └── json         # JSON adapters
│   │   ├── pages             # eBay application page objects
│   │   └── ui
│   │       ├── actions      # Selenium interaction services
│   │       ├── pages        # Reusable BasePage
│   │       └── waits        # Built-in and custom conditions
│   └── resources/config     # Framework configuration
└── test
    ├── java/com/luftborn/automation
    │   ├── data              # Typed test-data contracts
    │   ├── support           # BaseTest and reporting listeners
    │   └── tests             # Feature-oriented test packages
    └── resources
        ├── data              # Test data
        └── suites            # TestNG suites
```

## Run

Requirements: JDK 17+, Maven 3.9+, and Chrome/Firefox/Edge installed.

```powershell
mvn clean test
```

Override any configuration value without changing source files:

```powershell
mvn test -Dbrowser=firefox -Dheadless=true -Dbase.url=https://test.example.com
```

Generate/open an Allure report after installing the Allure CLI:

```powershell
allure serve target/allure-results
```

Maven also writes the TestNG/Surefire HTML and XML reports to `target/surefire-reports`. A verified headed run completed with one test passed and no failures or skips; its Allure result includes the obtained result count, final outcome, final filtered-page screenshot, and current URL.

## Locale note

The test intentionally selects `Transmission -> Manual` through eBay's left-side UI; it does not fabricate an aspect URL. eBay controls which facets are exposed based on live inventory, category, and visitor market. If eBay suppresses `Transmission` or falls back from Cars & Trucks to all categories for the active locale, the test fails with a clear environment/category message and attaches the browser state to Allure.

## Deliverables

Before submission:

1. Push this Maven project to the required GitHub or Bitbucket repository and replace the pending value below with its public/reviewer-accessible URL.
2. Record a headed run (`headless=false`) from browser launch through browser closure.
3. Include the successful console log and generated Allure report in the recording.

Remote repository URL: **Pending — no Git remote is configured in this checkout.**

Video URL/file: **Pending — record the final headed run using the checklist below.**

Detailed recording guidance is in `docs/VIDEO_PROOF.md`.

## Add a page and test

Page objects extend `BasePage`, keep locators private, and expose business operations rather than raw Selenium steps. Tests extend `BaseTest`. Put JSON under `src/test/resources/data`; use `ExcelDataReader.asDataProvider(...)` for a TestNG `@DataProvider`.

Configuration lives in `src/main/resources/config/framework.properties`. Test data belongs under `src/test/resources/data`, and TestNG suites under `src/test/resources/suites`. Do not place passwords or production secrets in repository configuration; pass those through CI environment/system-property injection.
