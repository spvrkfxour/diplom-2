package ru.yandex.praktikum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThan;


public class CreateOrderSteps {
    private final OrderSteps orderSteps = new OrderSteps();

    @Step("Get ingredients")
    public Response getIngredients() {
        return orderSteps.getIngredients();
    }

    @Step("Return correct body ingredients")
    public void getIngredientsReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(true))
                .body("data", hasSize(greaterThan(0)))
                .body("data._id", everyItem(notNullValue()));
        Allure.step("Response Body: " + response.getBody().asString());
    }
}
