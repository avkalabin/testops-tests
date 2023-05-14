package tests;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static specs.Spec.requestSpec;
import static specs.Spec.responseSpec;

public class TestcaseTests {

    final public static String PROJECT_ID = "2264",
            X_XSRF_TOKEN = "c5961690-4e63-40da-83df-f5eeb77ec9c4",
            ALLURE_TESTOPS_SESSION = "33047e42-5020-4a68-ac92-0caa0995d217";

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "https://allure.autotests.cloud";
        Configuration.holdBrowserOpen = true;

        RestAssured.baseURI = "https://allure.autotests.cloud";

    }

    @Test
    void createWithAPIAndUITest() {

        Faker faker = new Faker();
        String testCaseName = faker.name().fullName();

        step("Authorize");


        CreateTestCaseBody createTestCaseBody = new CreateTestCaseBody();
        createTestCaseBody.setName(testCaseName);

        CreateTestCaseResponse createTestCaseResponse = step("Create testcase", () -> given(requestSpec)
                .body(createTestCaseBody)
                .when()
                .post("/api/rs/testcasetree/leaf")
                .then()
                .spec(responseSpec)
                .extract().as(CreateTestCaseResponse.class));

        step("Verify testcase name", () -> {
            open("/favicon.ico");

            Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", ALLURE_TESTOPS_SESSION);
            getWebDriver().manage().addCookie(authorizationCookie);

            Integer testCaseId = createTestCaseResponse.getId();
            String testCaseUrl = format("/project/%s/test-cases/%s", PROJECT_ID, testCaseId);
            open(testCaseUrl);
            $(".TestCaseLayout__name").shouldHave(text(testCaseName));
        });

    }

    @Test
    void editTestCaseTest() {

        Faker faker = new Faker();
        String testCaseName = faker.name().fullName();


        CreateTestCaseBody createTestCaseBody = new CreateTestCaseBody();
        createTestCaseBody.setName(testCaseName);

        CreateTestCaseResponse createTestCaseResponse = step("Create testcase", () -> given(requestSpec)
                .body(createTestCaseBody)
                .when()
                .post("/api/rs/testcasetree/leaf")
                .then()
                .spec(responseSpec)
                .extract().as(CreateTestCaseResponse.class));

        step("Edit testcase name", () -> {
            open("/favicon.ico");

            Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", ALLURE_TESTOPS_SESSION);
            getWebDriver().manage().addCookie(authorizationCookie);

            Integer testCaseId = createTestCaseResponse.getId();
            String testCaseUrl = format("/project/%s/test-cases/%s", PROJECT_ID, testCaseId);
            open(testCaseUrl);
            $(".TestCaseLayout__name").shouldHave(text(testCaseName));
            $(".Menu__trigger").click();
            $("div:nth-child(1) .Menu__item > span").click();
            $(".FormLabel input[name='name']").setValue(" edited");
            $(".Button_size_base:nth-child(2)").click();

        });

        step("Verify testcase name", () -> {
            $(".TestCaseLayout__name").shouldHave(text(testCaseName + " edited"));
        });
    }

    @Test
    void createTestCaseStepsTest() {

        Faker faker = new Faker();
        String testCaseName = faker.name().fullName();

        CreateTestCaseBody createTestCaseBody = new CreateTestCaseBody();
        createTestCaseBody.setName(testCaseName);

        CreateTestCaseResponse createTestCaseResponse = step("Create testcase", () -> given(requestSpec)
                .body(createTestCaseBody)
                .when()
                .post("/api/rs/testcasetree/leaf")
                .then()
                .spec(responseSpec)
                .extract().as(CreateTestCaseResponse.class));


        CreateStepBody.Steps step1 = new CreateStepBody.Steps();
        step1.setName("Step 1");

        CreateStepBody.Steps step2 = new CreateStepBody.Steps();
        step2.setName("Step 2");

        CreateStepBody createStepBody = new CreateStepBody();
        List<CreateStepBody.Steps> stepBodies = new ArrayList<>();
        stepBodies.add(step1);
        stepBodies.add(step2);
        createStepBody.setSteps(stepBodies);

        Integer testCaseId = createTestCaseResponse.getId();
        String testCaseStepUrl = format("api/rs/testcase/%s/scenario", testCaseId);

        CreateStepResponse createStepResponse = step("Create steps", () -> given(requestSpec)
                .body(createStepBody)
                .when()
                .post(testCaseStepUrl)
                .then()
                .spec(responseSpec)
                .extract().as(CreateStepResponse.class));


        step("Verify steps creation", () -> {
            open("/favicon.ico");

            Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", ALLURE_TESTOPS_SESSION);
            getWebDriver().manage().addCookie(authorizationCookie);

            String testCaseUrl = format("/project/%s/test-cases/%s", PROJECT_ID, testCaseId);
            open(testCaseUrl);
            $(".TestCaseLayout__name").shouldHave(text(testCaseName));
        });

    }


}