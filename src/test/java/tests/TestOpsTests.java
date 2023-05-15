package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static tests.TestCaseManager.*;

public class TestOpsTests extends TestBase {

    TestCaseManager testCaseManager = new TestCaseManager();


    @DisplayName("Проверка имени созданного тест-кейса")
    @Test
    void verifyCreationTestCaseTest() {
        testCaseManager.createTestCase();
        testCaseManager.addSteps();

        step("Проверка имени созданного тест-кейса", () -> {
            open("/favicon.ico");

            Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", ALLURE_TESTOPS_SESSION);
            getWebDriver().manage().addCookie(authorizationCookie);

            Integer testCaseId = TestCaseManager.createTestCaseResponse.getId();
            String testCaseUrl = format("/project/%s/test-cases/%s", PROJECT_ID, testCaseId);
            open(testCaseUrl);
            $(".TestCaseLayout__name").shouldHave(text(testCaseManager.testCaseName));
        });

        testCaseManager.deleteTestCase();
    }

    @DisplayName("Проверка редактирования имени тест-кейса")
    @Test
    void editTestCaseTest() {

        testCaseManager.createTestCase();
        testCaseManager.addSteps();

        step("Редактирование имени тест-кейса", () -> {
            open("/favicon.ico");

            Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", ALLURE_TESTOPS_SESSION);
            getWebDriver().manage().addCookie(authorizationCookie);

            Integer testCaseId = createTestCaseResponse.getId();
            String testCaseUrl = format("/project/%s/test-cases/%s", PROJECT_ID, testCaseId);
            open(testCaseUrl);
            $(".TestCaseLayout__name").shouldHave(text(testCaseManager.testCaseName));
            $(".Menu__trigger").click();
            $("div:nth-child(1) .Menu__item > span").click();
            $(".FormLabel input[name='name']").setValue(" edited");
            $(".Button_size_base:nth-child(2)").click();
        });

        step("Проверка имени отредактированного тест-кейса", () -> {
            $(".TestCaseLayout__name").shouldHave(text(testCaseManager.testCaseName + " edited"));
        });

        testCaseManager.deleteTestCase();
    }


    @DisplayName("Проверка создания шагов тест-кейса")
    @Test
    void verifyCreationStepsTest() {

        testCaseManager.createTestCase();
        testCaseManager.addSteps();

        step("Проверка создания шагов тест-кейса", () -> {
            open("/favicon.ico");

            Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", ALLURE_TESTOPS_SESSION);
            getWebDriver().manage().addCookie(authorizationCookie);

            String testCaseUrl = format("/project/%s/test-cases/%s", PROJECT_ID, testCaseManager.testCaseId);
            open(testCaseUrl);
            $$(".Editable").shouldHave(sizeGreaterThanOrEqual(1));
        });

        testCaseManager.deleteTestCase();

    }


    @DisplayName("Проверка добавления аттача к шагам")
    @Test
    void editStepsAttachTest() {

        testCaseManager.createTestCase();
        testCaseManager.addSteps();

        step("Добавление аттача", () -> {
            open("/favicon.ico");

            Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", ALLURE_TESTOPS_SESSION);
            getWebDriver().manage().addCookie(authorizationCookie);

            Integer testCaseId = createTestCaseResponse.getId();
            String testCaseUrl = format("/project/%s/test-cases/%s", PROJECT_ID, testCaseId);
            open(testCaseUrl);
            $(".TestCaseLayout__name").shouldHave(text(testCaseManager.testCaseName));
            $("[data-testid='section__scenario'] button").click();
            $(".TestCaseScenarioStepEdit__wrapper [name = 'Step menu']").click();
            $(byTagAndText("span", "Attach text")).click();
            $(".FormLabel [name = 'content']").setValue("Attached text");
            $(".Form__controls:nth-child(3) .Button_style_primary > span").click();
            $(".Editable Button[type = 'submit']").click();
        });

        step("Проверка добавленного аттача", () -> {
            $(".Scenario").shouldHave(text("Attached text"));
        });

        testCaseManager.deleteTestCase();
    }


}