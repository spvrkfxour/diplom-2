package ru.yandex.praktikum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.dto.CreateUserRequest;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyString;
import static ru.yandex.praktikum.env.EnvConst.*;


public class CreateUserSteps {
    private final UserSteps userSteps = new UserSteps();

    @Step("Create user")
    public Response createUser(CreateUserRequest request) {
        return userSteps.createUser(request);
    }

    @Step("Return correct body")
    public void createUserReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .body("refreshToken", notNullValue())
                .body("accessToken", notNullValue())
                .body("accessToken", not(emptyString()));
        Allure.step("Response Body: " + response.getBody().asString());
    }

    @Step("Return correct body already exists")
    public void createUserThatAlreadyBeenCreatedReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(false))
                .body("message", equalTo(CREATE_USER_ALREADY_EXISTS_MSG_ERROR));
        Allure.step("Response Body: " + response.getBody().asString());
    }

    @Step("Return correct body without parameter")
    public void createUserWithoutParamReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(false))
                .body("message", equalTo(CREATE_USER_WITHOUT_PARAM_MSG_ERROR));
        Allure.step("Response Body: " + response.getBody().asString());
    }
}
