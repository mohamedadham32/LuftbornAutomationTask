# Video proof checklist

Use any screen recorder (for example, TinyTake, OBS, or the Windows Snipping Tool screen recorder).

1. Confirm `headless=false` in `src/main/resources/config/framework.properties`.
2. Start recording with the terminal visible.
3. Run `mvn clean test`.
4. Capture Chrome opening, loading eBay, searching, displaying results, selecting `Transmission -> Manual`, and closing.
5. Keep recording long enough to show Maven's final test status and the execution step log.
6. Optionally run `allure serve target/allure-results` and capture the scenario steps and attachments.
7. Stop recording and upload the video using the submission channel requested by the reviewer.

Do not cut away the initial browser launch or final browser closure; both are explicitly required by the task.
