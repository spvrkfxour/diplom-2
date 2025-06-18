package ru.yandex.praktikum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;


public class StatusCodeSteps {

    @Step("Return body correct status code - 200")
    public void return200Test(Response response) {
        response.then().statusCode(200);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }

    @Step("Return body correct status code - 403")
    public void return403Test(Response response) {
        response.then().statusCode(403);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }
}
