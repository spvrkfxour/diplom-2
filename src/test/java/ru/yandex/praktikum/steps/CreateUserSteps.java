package ru.yandex.praktikum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.dto.CreateUserRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


public class CreateUserSteps {
    private final UserSteps userSteps = new UserSteps();

    @Step("Create user")
    public Response createUserStepTest(CreateUserRequest request) {
        Response response = userSteps.createUser(request);
        return response;
    }

    @Step("Return correct body")
    public void createUserReturnCorrectBodyTest(Response response) {
        response.then()
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
        Allure.step("Response Body: " + response.getBody().asString());
    }
}
