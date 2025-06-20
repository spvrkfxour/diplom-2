package ru.yandex.praktikum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


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
                .body("data", notNullValue())
                .body("data._id", notNullValue());
        Allure.step("Response Body: " + response.getBody().asString());
    }
}
