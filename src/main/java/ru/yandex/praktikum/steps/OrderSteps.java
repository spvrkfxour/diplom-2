package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.dto.CreateOrderRequest;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.env.EnvConst.*;


public class OrderSteps {

    public Response getIngredients() {

        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .when()
                .get(GET_INGREDIENTS_ENDPOINT);
    }

    public Response createOrder(CreateOrderRequest request) {

        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(request)
                .when()
                .post(CREATE_ORDER_ENDPOINT);
    }
}
