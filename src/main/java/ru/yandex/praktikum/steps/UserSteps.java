package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.dto.CreateUserRequest;
import ru.yandex.praktikum.dto.LoginUserRequest;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.env.EnvConst.*;


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

    public Response deleteUser(String accessToken) {

        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .when()
                .delete(DELETE_USER_ENDPOINT);
    }
}
