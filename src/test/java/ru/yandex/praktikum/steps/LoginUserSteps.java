package ru.yandex.praktikum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.dto.LoginUserRequest;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyString;
import static ru.yandex.praktikum.env.EnvConst.LOGIN_USER_WITH_WRONG_PARAM_MSG_ERROR;


public class LoginUserSteps {
    private final UserSteps userSteps = new UserSteps();

    @Step("Login user")
    public Response loginUser(LoginUserRequest request) {
        return userSteps.loginUser(request);
    }

    @Step("Return correct body")
    public void loginUserReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user", notNullValue())
                .body("accessToken", not(emptyString()));
        Allure.step("Response Body: " + response.getBody().asString());
    }

    @Step("Return correct body with wrong parameter")
    public void loginUserWithWrongParamReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(false))
                .body("message", equalTo(LOGIN_USER_WITH_WRONG_PARAM_MSG_ERROR));
        Allure.step("Response Body: " + response.getBody().asString());
    }
}
