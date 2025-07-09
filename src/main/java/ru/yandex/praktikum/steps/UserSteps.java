package ru.yandex.praktikum.steps;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.env.EnvConst.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.dto.CreateUserRequest;
import ru.yandex.praktikum.dto.LoginUserRequest;
import ru.yandex.praktikum.dto.UpdateUserRequest;


public class UserSteps {

    public Response createUser(CreateUserRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(request)
                .when()
                .post(CREATE_USER_ENDPOINT);
    }

    public String getAccessToken(Response response) {
        return response.path("accessToken");
    }

    public Response loginUser(LoginUserRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(request)
                .when()
                .post(LOGIN_USER_ENDPOINT);
    }

    public Response getUserInfo(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .when()
                .get(GET_USER_INFO_ENDPOINT);
    }

    public Response getUserInfoWithoutToken() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .when()
                .get(GET_USER_INFO_ENDPOINT);
    }

    public Response updateAuthUserInfo(UpdateUserRequest request, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(request)
                .when()
                .patch(UPDATE_USER_ENDPOINT);
    }

    public Response updateNotAuthUserInfoWithoutToken(UpdateUserRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(request)
                .when()
                .patch(UPDATE_USER_ENDPOINT);
    }

    public Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .when()
                .delete(DELETE_USER_ENDPOINT);
    }
}
