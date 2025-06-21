package ru.yandex.praktikum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;


public class StatusCodeSteps {

    @Step("Return body correct status code - 200")
    public void return200(Response response) {
        response.then().statusCode(200);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }

    @Step("Return body correct status code - 400")
    public void return400(Response response) {
        response.then().statusCode(400);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }

    @Step("Return body correct status code - 401")
    public void return401(Response response) {
        response.then().statusCode(401);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }

    @Step("Return body correct status code - 403")
    public void return403(Response response) {
        response.then().statusCode(403);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }

    @Step("Return body correct status code - 500")
    public void return500(Response response) {
        response.then().statusCode(500);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }
}
