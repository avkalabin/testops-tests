package helpers;

import org.openqa.selenium.Cookie;
import tests.TestCaseManager;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static java.lang.String.format;
import static tests.TestCaseManager.ALLURE_TESTOPS_SESSION;
import static tests.TestCaseManager.PROJECT_ID;

public class CookieAuth {

    public void authWithCookie() {
        open("/favicon.ico");

        Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", ALLURE_TESTOPS_SESSION);
        getWebDriver().manage().addCookie(authorizationCookie);

        Integer testCaseId = TestCaseManager.createTestCaseResponse.getId();
        String testCaseUrl = format("/project/%s/test-cases/%s", PROJECT_ID, testCaseId);
        open(testCaseUrl);
    }
}
