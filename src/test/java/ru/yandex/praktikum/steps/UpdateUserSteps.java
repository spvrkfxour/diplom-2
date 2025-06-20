package ru.yandex.praktikum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.dto.UpdateUserRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.yandex.praktikum.env.EnvConst.GET_NOT_AUTH_USER_INFO_MSG_ERROR;
import static ru.yandex.praktikum.env.EnvConst.UPDATE_USER_WITH_EMAIL_THAT_ALREADY_TAKEN_MSG_ERROR;


public class UpdateUserSteps {
    private final UserSteps userSteps = new UserSteps();

    @Step("Get user`s info")
    public Response getUserInfo(String accessToken) {
        return userSteps.getUserInfo(accessToken);
    }

    @Step("Get user`s info without token")
    public Response getUserInfoWithoutToken() {
        return userSteps.getUserInfoWithoutToken();
    }

    @Step("Update auth user email")
    public Response updateAuthUserEmail(UpdateUserRequest request, String accessToken) {
        return userSteps.updateAuthUserEmail(request, accessToken);
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

    @Step("Return correct body with update user")
    public void updateUserReturnCorrectBody(Response response, String email, String name) {
        response.then()
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name));
        Allure.step("Response Body: " + response.getBody().asString());
    }

    @Step("Return correct body update user with email that already been taken")
    public void getAuthUserUpdateEmailThatAlreadyBeenTakenReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(false))
                .body("message", equalTo(UPDATE_USER_WITH_EMAIL_THAT_ALREADY_TAKEN_MSG_ERROR));
        Allure.step("Response Body: " + response.getBody().asString());
    }
}
