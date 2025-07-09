package ru.yandex.praktikum;

import static ru.yandex.praktikum.env.EnvConst.DOMAINS;

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


public class GetUserOrdersTest {
    private final UserSteps user = new UserSteps();
    private final CreateUserSteps createUser = new CreateUserSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private final LoginUserSteps loginUser = new LoginUserSteps();
    private final CreateOrderSteps createOrder = new CreateOrderSteps();
    private final UpdateUserSteps updateUser = new UpdateUserSteps();
    private final GetUserOrdersSteps getUserOrders = new GetUserOrdersSteps();
    private CreateOrderRequest createFirstOrderRequest;
    private CreateOrderRequest createSecondOrderRequest;
    private final List<String> firstOrderIngredients = new ArrayList<>();
    private final List<String> secondOrderIngredients = new ArrayList<>();
    private String accessToken;
    private String email;
    private String password;
    private String name;

    @Before
    public void setUp() {
        generateTestData();
        createTestUser();
        loginTestUser();
        userCreateOrders();
    }

    private void generateTestData() {
        email = generateRandomString(3, 12) + DOMAINS[ThreadLocalRandom.current().nextInt(DOMAINS.length)];
        password = generateRandomString(3, 12);
        name = generateRandomString(3, 12);

        String firstIngredientFirstOrder = createOrder.getRandomIngredientId();
        String secondIngredientFirstOrder = createOrder.getRandomIngredientId();
        String thirdIngredientFirstOrder = createOrder.getRandomIngredientId();

        Collections.addAll(firstOrderIngredients, firstIngredientFirstOrder ,
                secondIngredientFirstOrder, thirdIngredientFirstOrder);
        createFirstOrderRequest = new CreateOrderRequest(firstOrderIngredients);

        String firstIngredientSecondOrder = createOrder.getRandomIngredientId();
        String secondIngredientSecondOrder = createOrder.getRandomIngredientId();

        Collections.addAll(secondOrderIngredients, firstIngredientSecondOrder ,
                secondIngredientSecondOrder);
        createSecondOrderRequest = new CreateOrderRequest(secondOrderIngredients);
    }

    private String generateRandomString(int minLen, int maxLen) {
        return RandomStringUtils.randomAlphanumeric(minLen, maxLen).toLowerCase();
    }

    private void createTestUser() {
        CreateUserRequest createRequest = new CreateUserRequest(email, password, name);
        Response response = createUser.createUser(createRequest);
        accessToken = user.getAccessToken(response);
    }

    private void loginTestUser() {
        LoginUserRequest loginRequest = new LoginUserRequest(email, password);
        loginUser.loginUser(loginRequest);
    }

    private void userCreateOrders() {
        createOrder.createOrderWithToken(createFirstOrderRequest, accessToken);
        createOrder.createOrderWithToken(createSecondOrderRequest, accessToken);
    }

    @Test
    @DisplayName("Get all orders for auth user")
    @Description("Create user with random valid email, password and name parameters. " +
            "Login this user. Create valid orders by user and Success Get user`s orders. " +
            "GET https://stellarburgers.nomoreparties.site/api/orders")
    public void getAuthUserOrdersTest() {
        Response response = getUserOrders.getAuthUserOrdersWithToken(accessToken);
        statusCode.return200(response);
        getUserOrders.getAuthUserOrdersReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Get all orders for not auth user")
    @Description("Create user with random valid email, password and name parameters. " +
            "Create valid orders by user and Failed Get not auth user`s orders. " +
            "GET https://stellarburgers.nomoreparties.site/api/orders")
    public void getNotAuthUserOrdersTest() {
        Response response = getUserOrders.getAuthUserOrdersWithoutToken();
        statusCode.return401(response);
        updateUser.getNotAuthUserInfoReturnCorrectBody(response);
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
