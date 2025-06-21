package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.dto.CreateOrderRequest;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

    public Response createOrderWithToken(CreateOrderRequest request, String accessToken) {

        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(request)
                .when()
                .post(CREATE_ORDER_ENDPOINT);
    }

    public Response createOrderWithoutToken(CreateOrderRequest request) {

        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(request)
                .when()
                .post(CREATE_ORDER_ENDPOINT);
    }

    public String getRandomIngredientId() {

        List<String> ingredientsList = given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .when()
                .get(GET_INGREDIENTS_ENDPOINT)
                .then()
                .extract()
                .jsonPath()
                .getList("data._id");

        return ingredientsList.get(ThreadLocalRandom.current().nextInt(ingredientsList.size()));
    }

    public Response getUserOrdersWithToken(String accessToken) {

        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .when()
                .get(GET_USER_ORDERS_ENDPOINT);
    }

    public Response getUserOrdersWithoutToken() {

        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .when()
                .get(GET_USER_ORDERS_ENDPOINT);
    }
}
