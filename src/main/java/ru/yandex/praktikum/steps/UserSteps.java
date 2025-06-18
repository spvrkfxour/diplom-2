package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.dto.CreateUserRequest;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.env.EnvConst.*;


public class UserSteps {

    public Response createUser(CreateUserRequest request) {

        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(request)
                .when()
                .post(USER_CREATE_ENDPOINT);
    }
}
