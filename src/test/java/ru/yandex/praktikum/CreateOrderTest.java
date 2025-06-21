package ru.yandex.praktikum;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.dto.CreateOrderRequest;
import ru.yandex.praktikum.dto.CreateUserRequest;
import ru.yandex.praktikum.dto.LoginUserRequest;
import ru.yandex.praktikum.steps.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static ru.yandex.praktikum.env.EnvConst.DOMAINS;


public class CreateOrderTest {
    private final UserSteps user = new UserSteps();
    private final CreateUserSteps createUser = new CreateUserSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private final LoginUserSteps loginUser = new LoginUserSteps();
    private final CreateOrderSteps createOrder = new CreateOrderSteps();
    private final UpdateUserSteps updateUser = new UpdateUserSteps();
    private CreateUserRequest createRequest;
    private LoginUserRequest loginRequest;
    private CreateOrderRequest createOrderRequest;
    private final List<String> ingredients = new ArrayList<>();
    private String accessToken;
    private String email;
    private String password;
    private String name;

    private String generateRandomString(int minLen, int maxLen) {
        return RandomStringUtils.randomAlphanumeric(minLen, maxLen).toLowerCase();
    }

    @Before
    public void setUp() {
        email = generateRandomString(3, 12) + DOMAINS[ThreadLocalRandom.current().nextInt(DOMAINS.length)];
        password = generateRandomString(3, 12).toLowerCase();
        name = generateRandomString(3, 12).toLowerCase();
        createRequest = new CreateUserRequest(email, password, name);
        Response response = createUser.createUser(createRequest);
        accessToken = user.getAccessToken(response);
    }

    @Test
    @DisplayName("Get ingredients")
    @Description("Get all ingredients. GET https://stellarburgers.nomoreparties.site/api/ingredients")
    public void getIngredientsTest() {
        Response response = createOrder.getIngredients();
        statusCode.return200(response);
        createOrder.getIngredientsReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create order auth user")
    @Description("Auth user Create order with random ingredients from ingredients list. POST https://stellarburgers.nomoreparties.site/api/orders")
    public void createOrderWithAuthUserTest() {
        loginRequest = new LoginUserRequest(email, password);
        loginUser.loginUser(loginRequest);
        String firstIngredient = createOrder.getRandomIngredientId();
        String secondIngredient = createOrder.getRandomIngredientId();
        String thirdIngredient = createOrder.getRandomIngredientId();
        Collections.addAll(ingredients, firstIngredient, secondIngredient, thirdIngredient);
        createOrderRequest = new CreateOrderRequest(ingredients);
        Response response = createOrder.createOrderWithToken(createOrderRequest, accessToken);
        statusCode.return200(response);
        createOrder.createOrderReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create order not auth user")
    @Description("Not Auth user Create order with random ingredients from ingredients list. POST https://stellarburgers.nomoreparties.site/api/orders")
    public void createOrderWithNotAuthUserTest() {
        String firstIngredient = createOrder.getRandomIngredientId();
        Collections.addAll(ingredients, firstIngredient);
        createOrderRequest = new CreateOrderRequest(ingredients);
        Response response = createOrder.createOrderWithoutToken(createOrderRequest);
        statusCode.return401(response);
        updateUser.getNotAuthUserInfoReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create order with empty ingredients")
    @Description("Auth user Create order with empty ingredients list. POST https://stellarburgers.nomoreparties.site/api/orders")
    public void createOrderWithEmptyIngredientsTest() {
        loginRequest = new LoginUserRequest(email, password);
        loginUser.loginUser(loginRequest);
        createOrderRequest = new CreateOrderRequest(ingredients);
        Response response = createOrder.createOrderWithToken(createOrderRequest, accessToken);
        statusCode.return400(response);
        createOrder.createEmptyOrderReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create order with null ingredients")
    @Description("Auth user Create order with null ingredients list. POST https://stellarburgers.nomoreparties.site/api/orders")
    public void createOrderWithNullIngredientsTest() {
        loginRequest = new LoginUserRequest(email, password);
        loginUser.loginUser(loginRequest);
        createOrderRequest = new CreateOrderRequest(null);
        Response response = createOrder.createOrderWithToken(createOrderRequest, accessToken);
        statusCode.return400(response);
        createOrder.createEmptyOrderReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create order with wrong hash id ingredient")
    @Description("Auth user Create order with wrong hash id ingredient from ingredients list. POST https://stellarburgers.nomoreparties.site/api/orders")
    public void createOrderWithWrongHashIdIngredientTest() {
        loginRequest = new LoginUserRequest(email, password);
        loginUser.loginUser(loginRequest);
        String firstIngredient = createOrder.getRandomIngredientId();
        Collections.addAll(ingredients, firstIngredient + "error");
        createOrderRequest = new CreateOrderRequest(ingredients);
        Response response = createOrder.createOrderWithToken(createOrderRequest, accessToken);
        statusCode.return500(response);
    }

    @After
    @Step("Delete user")
    public void tearDown() {
        if (accessToken != null) {
            Response response = user.deleteUser(accessToken);
            Allure.step("Response Body: " + response.getBody().asString());
        }
    }
}
