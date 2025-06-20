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
import ru.yandex.praktikum.steps.*;

import java.util.concurrent.ThreadLocalRandom;

import static ru.yandex.praktikum.env.EnvConst.DOMAINS;


public class UpdateUserTest {
    private final UserSteps user = new UserSteps();
    private final CreateUserSteps createUser = new CreateUserSteps();
    private final LoginUserSteps loginUser = new LoginUserSteps();
    private final GetUserInfoSteps getUserInfo = new GetUserInfoSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
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
    }

    @Test
    @DisplayName("Get auth user email and name")
    @Description("Create user with random valid email, password and name parameters. " +
            "Login this user and Success Get user`s email and name " +
            "GET https://stellarburgers.nomoreparties.site/api/auth/user")
    public void getAuthUserInfoTest() {
        loginRequest = new LoginUserRequest(email, password);
        loginUser.loginUser(loginRequest);
        Response response = getUserInfo.getUserInfo(accessToken);
        statusCode.return200(response);
        getUserInfo.getAuthUserInfoReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Get user email and name without auth")
    @Description("Create user with random valid email, password and name parameters " +
            "and Failed Get user`s email and name without auth " +
            "GET https://stellarburgers.nomoreparties.site/api/auth/user")
    public void getNotAuthUserInfoTest() {
        Response response = getUserInfo.getUserInfoWithoutToken();
        statusCode.return401(response);
        getUserInfo.getNotAuthUserInfoReturnCorrectBody(response);
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
