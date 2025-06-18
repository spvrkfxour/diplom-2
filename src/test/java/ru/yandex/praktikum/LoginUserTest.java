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
import ru.yandex.praktikum.dto.LoginUserRequest;
import ru.yandex.praktikum.steps.CreateUserSteps;
import ru.yandex.praktikum.steps.LoginUserSteps;
import ru.yandex.praktikum.steps.StatusCodeSteps;
import ru.yandex.praktikum.steps.UserSteps;

import java.util.concurrent.ThreadLocalRandom;

import static ru.yandex.praktikum.env.EnvConst.DOMAINS;


public class LoginUserTest {
    private final UserSteps user = new UserSteps();
    private final CreateUserSteps createUser = new CreateUserSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private final LoginUserSteps loginUser = new LoginUserSteps();
    private CreateUserRequest createRequest;
    private LoginUserRequest loginRequest;
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
        createUser.checkAccessToken(accessToken);
    }

    @Test
    @DisplayName("Login user with random valid parameters")
    @Description("Create user with random valid email, password and name parameters and Login this user. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/login")
    public void loginValidUserTest() {
        loginRequest = new LoginUserRequest(email, password);
        Response response = loginUser.loginUser(loginRequest);
        statusCode.return200(response);
        loginUser.loginUserReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Login user with wrong email")
    @Description("Create user with random valid email, password and name parameters and Login this user with wrong email. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/login")
    public void loginUserWithWrongEmailTest() {
        loginRequest = new LoginUserRequest(generateRandomString(3, 12)
                + DOMAINS[ThreadLocalRandom.current().nextInt(DOMAINS.length)], password);
        Response response = loginUser.loginUser(loginRequest);
        statusCode.return401(response);
        loginUser.loginUserWithWrongParamReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Login user with wrong password")
    @Description("Create user with random valid email, password and name parameters and Login this user with wrong password. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/login")
    public void loginUserWithWrongPasswordTest() {
        loginRequest = new LoginUserRequest(email, generateRandomString(3, 12).toLowerCase());
        Response response = loginUser.loginUser(loginRequest);
        statusCode.return401(response);
        loginUser.loginUserWithWrongParamReturnCorrectBody(response);
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
