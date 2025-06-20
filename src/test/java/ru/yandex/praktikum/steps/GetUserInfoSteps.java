package ru.yandex.praktikum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.yandex.praktikum.env.EnvConst.GET_NOT_AUTH_USER_INFO_MSG_ERROR;


public class GetUserInfoSteps {
    private final UserSteps userSteps = new UserSteps();

    @Step("Get user`s info")
    public Response getUserInfo(String accessToken) {
        return userSteps.getUserInfo(accessToken);
    }

    @Step("Get user`s info without token")
    public Response getUserInfoWithoutToken() {
        return userSteps.getUserInfoWithoutToken();
    }

    @Step("Return correct body")
    public void getAuthUserInfoReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(true))
                .body("user", notNullValue());
        Allure.step("Response Body: " + response.getBody().asString());
    }

    @Step("Return correct body without auth")
    public void getNotAuthUserInfoReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(false))
                .body("message", equalTo(GET_NOT_AUTH_USER_INFO_MSG_ERROR));
        Allure.step("Response Body: " + response.getBody().asString());
    }
}
