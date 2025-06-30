package ru.yandex.praktikum.steps;

import static org.apache.http.HttpStatus.*;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;


public class StatusCodeSteps {

    @Step("Return body correct status code - 200 OK")
    public void return200(Response response) {
        response.then().statusCode(SC_OK);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }

    @Step("Return body correct status code - 400 BAD_REQUEST")
    public void return400(Response response) {
        response.then().statusCode(SC_BAD_REQUEST);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }

    @Step("Return body correct status code - 401 UNAUTHORIZED")
    public void return401(Response response) {
        response.then().statusCode(SC_UNAUTHORIZED);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }

    @Step("Return body correct status code - 403 FORBIDDEN")
    public void return403(Response response) {
        response.then().statusCode(SC_FORBIDDEN);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }

    @Step("Return body correct status code - 500 INTERNAL_SERVER_ERROR")
    public void return500(Response response) {
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
        Allure.step("Response Status Code: " + response.getStatusCode());
    }
}
