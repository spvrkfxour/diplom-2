package ru.yandex.praktikum.steps;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;


public class GetUserOrdersSteps {
    private final OrderSteps orderSteps = new OrderSteps();

    @Step("Get orders for auth user")
    public Response getAuthUserOrdersWithToken(String accessToken) {
        return orderSteps.getUserOrdersWithToken(accessToken);
    }

    @Step("Get orders for not auth user")
    public Response getAuthUserOrdersWithoutToken() {
        return orderSteps.getUserOrdersWithoutToken();
    }

    @Step("Get orders for auth user return correct body")
    public void getAuthUserOrdersReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(true))
                .body("orders", hasSize(greaterThan(0)))
                .body("orders", everyItem(notNullValue()))
                .body("orders", everyItem(not(emptyString())))
                .body("total", notNullValue())
                .body("total", not(emptyString()))
                .body("totalToday", notNullValue())
                .body("totalToday", not(emptyString()));
        Allure.step("Response Body: " + response.getBody().asString());
    }
}
