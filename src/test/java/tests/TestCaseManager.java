package tests;

import com.github.javafaker.Faker;
import models.*;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static specs.Spec.requestSpec;
import static specs.Spec.responseSpec;

public class TestCaseManager {

    final static public String PROJECT_ID = "2264",
            X_XSRF_TOKEN = "075c0ca2-b053-4926-90ad-f1f877dab709",
            ALLURE_TESTOPS_SESSION = "ff995ffd-4e7d-45ae-9e23-653f6ff467de";

    public static CreateTestCaseResponse createTestCaseResponse;
    Integer testCaseId;
    Faker faker = new Faker();
    String testCaseName = faker.name().fullName();

    @DisplayName("Создание тест-кейса")
    void createTestCase() {

        CreateTestCaseBody createTestCaseBody = new CreateTestCaseBody();
        createTestCaseBody.setName(testCaseName);

        createTestCaseResponse = step("Создание тест-кейса", () -> given(requestSpec)
                .body(createTestCaseBody)
                .when()
                .post("/api/rs/testcasetree/leaf")
                .then()
                .spec(responseSpec)
                .extract().as(CreateTestCaseResponse.class));

    }

    @DisplayName("Добавление шагов в тест-кейс")
    void addSteps() {

        CreateStepBody.Steps step1 = new CreateStepBody.Steps();
        step1.setName("Step 1");

        CreateStepBody.Steps step2 = new CreateStepBody.Steps();
        step2.setName("Step 2");

        CreateStepBody createStepBody = new CreateStepBody();
        List<CreateStepBody.Steps> steps = new ArrayList<>();
        steps.add(step1);
        steps.add(step2);
        createStepBody.setSteps(steps);

        testCaseId = createTestCaseResponse.getId();
        String testCaseStepUrl = format("api/rs/testcase/%s/scenario", testCaseId);

        step("Добавление шагов в тест-кейс", () -> given(requestSpec)
                .body(createStepBody)
                .when()
                .post(testCaseStepUrl)
                .then()
                .spec(responseSpec)
                .extract().as(CreateStepResponse.class));
    }

    @DisplayName("Удаление тест-кейса")
    void deleteTestCase() {

        DeleteStepCaseBody deleteStepCaseBody = new DeleteStepCaseBody();
        DeleteStepCaseBody.Selection selection = new DeleteStepCaseBody.Selection();
        selection.setInverted(false);
        selection.setLeafsInclude(new int[]{testCaseId});
        selection.setKind("TreeSelectionDto");
        selection.setProjectId(2264);
        deleteStepCaseBody.setSelection(selection);

        step("Удаление тест-кейса", () -> given(requestSpec)
                .body(deleteStepCaseBody)
                .when()
                .post("/api/rs/testcase/bulk/remove")
                .then()
                .log().status()
                .log().body()
                .statusCode(204));
    }
}
