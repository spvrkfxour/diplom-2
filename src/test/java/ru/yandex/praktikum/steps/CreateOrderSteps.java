package ru.yandex.praktikum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.dto.CreateOrderRequest;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThan;
import static ru.yandex.praktikum.env.EnvConst.CREATE_EMPTY_ORDER_MSG_ERROR;


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

    @Step("Get random ingredient")
    public String getRandomIngredientId() {
        String id = orderSteps.getRandomIngredientId();
        Allure.step("id: " + id);
        return id;
    }

    @Step("Auth user create order")
    public Response createOrderWithToken(CreateOrderRequest request, String accessToken) {
        return orderSteps.createOrderWithToken(request, accessToken);
    }

    @Step("Not auth user create order")
    public Response createOrderWithoutToken(CreateOrderRequest request) {
        return orderSteps.createOrderWithoutToken(request);
    }

    @Step("Create order by auth user return correct body")
    public void createOrderReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("name", not(emptyString()))
                .body("order.number", notNullValue())
                .body("order.number", not(emptyString()));
        Allure.step("Response Body: " + response.getBody().asString());
    }

    @Step("Create empty order return correct body")
    public void createEmptyOrderReturnCorrectBody(Response response) {
        response.then()
                .body("success", equalTo(false))
                .body("message", equalTo(CREATE_EMPTY_ORDER_MSG_ERROR));
        Allure.step("Response Body: " + response.getBody().asString());
    }
}
