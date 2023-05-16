package tests;

import helpers.CookieAuth;
import org.junit.jupiter.api.*;
import pages.TestOpsPage;

import static io.qameta.allure.Allure.step;

public class TestOpsTests extends TestBase {

    TestCaseManager testCaseManager = new TestCaseManager();
    TestOpsPage testOpsPage = new TestOpsPage();
    CookieAuth cookieAuth = new CookieAuth();


    @DisplayName("Проверка имени созданного тест-кейса")
    @Test
    void verifyCreationTestCaseTest() {

        step("Авторизация", () -> cookieAuth.authWithCookie());

        testCaseManager.createTestCase();
        testCaseManager.addSteps();

        step("Проверка имени созданного тест-кейса", () -> {

            testOpsPage.verifyTestCaseName(testCaseManager.testCaseName);
        });

        testCaseManager.deleteTestCase();
    }

    @DisplayName("Проверка редактирования имени тест-кейса")
    @Test
    void editTestCaseTest() {

        step("Авторизация", () -> cookieAuth.authWithCookie());

        testCaseManager.createTestCase();
        testCaseManager.addSteps();

        step("Редактирование имени тест-кейса", () -> {

            testOpsPage.verifyTestCaseName(testCaseManager.testCaseName)
                    .clickRenameTestCase()
                    .editTestCaseName();

        });

        step("Проверка имени отредактированного тест-кейса", () -> {
            testOpsPage.verifyTestCaseName(testCaseManager.testCaseName + " edited");
        });

        testCaseManager.deleteTestCase();
    }


    @DisplayName("Проверка создания шагов тест-кейса")
    @Test
    void verifyCreationStepsTest() {

        step("Авторизация", () -> cookieAuth.authWithCookie());

        testCaseManager.createTestCase();
        testCaseManager.addSteps();

        step("Проверка создания шагов тест-кейса", () -> {

            testOpsPage.verifyStepsCreation();
        });

        testCaseManager.deleteTestCase();

    }

    @DisplayName("Проверка добавления аттача к шагам")
    @Test
    void editStepsAttachTest() {

        step("Авторизация", () -> cookieAuth.authWithCookie());

        testCaseManager.createTestCase();
        testCaseManager.addSteps();

        step("Добавление аттача", () -> {

            testOpsPage.verifyTestCaseName(testCaseManager.testCaseName)
                    .editStepsMenuClick()
                    .attachTextToStep("Attached text");

        });

        step("Проверка добавленного аттача", () -> {
            testOpsPage.verifyAddedAttach("Attached text");
        });

        testCaseManager.deleteTestCase();
    }

}