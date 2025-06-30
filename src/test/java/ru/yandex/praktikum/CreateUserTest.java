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
import ru.yandex.praktikum.dto.CreateUserRequest;
import ru.yandex.praktikum.steps.CreateUserSteps;
import ru.yandex.praktikum.steps.StatusCodeSteps;
import ru.yandex.praktikum.steps.UserSteps;

import java.util.concurrent.ThreadLocalRandom;

import static ru.yandex.praktikum.env.EnvConst.*;


public class CreateUserTest {
    private final UserSteps user = new UserSteps();
    private final CreateUserSteps createUser = new CreateUserSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private CreateUserRequest createRequest;
    private String accessToken;
    private String email;
    private String password;
    private String name;
    private Response response;

    private String generateRandomString(int minLen, int maxLen) {
        return RandomStringUtils.randomAlphanumeric(minLen, maxLen).toLowerCase();
    }

    @Before
    public void setUp() {
        email = generateRandomString(3, 12) + DOMAINS[ThreadLocalRandom.current().nextInt(DOMAINS.length)];
        password = generateRandomString(3, 12).toLowerCase();
        name = generateRandomString(3, 12).toLowerCase();
    }

    @Test
    @DisplayName("Create user with random valid parameters")
    @Description("Success create user with random valid email, password and name parameters. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/register")
    public void createValidUserTest() {
        createRequest = new CreateUserRequest(email, password, name);
        response = createUser.createUser(createRequest);
        statusCode.return200(response);
        createUser.createUserReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create user with already created user`s parameters")
    @Description("Failed create user with already created user`s email, password and name parameters. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/register")
    public void createUserThatAlreadyBeenCreatedTest() {
        createRequest = new CreateUserRequest(email, password, name);
        createUser.createUser(createRequest);
        response = createUser.createUser(createRequest);
        statusCode.return403(response);
        createUser.createUserThatAlreadyBeenCreatedReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create user with empty email parameter")
    @Description("Failed create user with empty email and random valid password and name parameters. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/register")
    public void createUserWithEmptyEmailTest() {
        createRequest = new CreateUserRequest("", password, name);
        response = createUser.createUser(createRequest);
        statusCode.return403(response);
        createUser.createUserWithoutParamReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create user with empty password parameter")
    @Description("Failed create user with empty password and random valid email and name parameters. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/register")
    public void createUserWithEmptyPasswordTest() {
        createRequest = new CreateUserRequest(email, "", name);
        response = createUser.createUser(createRequest);
        statusCode.return403(response);
        createUser.createUserWithoutParamReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create user with empty name parameter")
    @Description("Failed create user with empty name and random valid email and password parameters. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/register")
    public void createUserWithEmptyNameTest() {
        createRequest = new CreateUserRequest(email, password, "");
        response = createUser.createUser(createRequest);
        statusCode.return403(response);
        createUser.createUserWithoutParamReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create user with null email parameter")
    @Description("Failed create user with null email and random valid password and name parameters. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/register")
    public void createUserWithNullEmailTest() {
        createRequest = new CreateUserRequest(null, password, name);
        response = createUser.createUser(createRequest);
        statusCode.return403(response);
        createUser.createUserWithoutParamReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create user with null password parameter")
    @Description("Failed create user with null password and random valid email and name parameters. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/register")
    public void createUserWithNullPasswordTest() {
        createRequest = new CreateUserRequest(email, null, name);
        response = createUser.createUser(createRequest);
        statusCode.return403(response);
        createUser.createUserWithoutParamReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Create user with null name parameter")
    @Description("Failed create user with null name and random valid email and password parameters. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/register")
    public void createUserWithNullNameTest() {
        createRequest = new CreateUserRequest(email, password, null);
        response = createUser.createUser(createRequest);
        statusCode.return403(response);
        createUser.createUserWithoutParamReturnCorrectBody(response);
    }

    @After
    @Step("Delete user")
    public void tearDown() {
        accessToken = user.getAccessToken(response);
        if (accessToken != null) {
            Response response = user.deleteUser(accessToken);
            Allure.step("Response Body: " + response.getBody().asString());
        }
    }
}
