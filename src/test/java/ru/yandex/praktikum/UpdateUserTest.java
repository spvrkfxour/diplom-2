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
import ru.yandex.praktikum.dto.UpdateUserRequest;
import ru.yandex.praktikum.steps.*;

import java.util.concurrent.ThreadLocalRandom;

import static ru.yandex.praktikum.env.EnvConst.DOMAINS;


public class UpdateUserTest {
    private final UserSteps user = new UserSteps();
    private final CreateUserSteps createUser = new CreateUserSteps();
    private final LoginUserSteps loginUser = new LoginUserSteps();
    private final UpdateUserSteps updateUser = new UpdateUserSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private CreateUserRequest createRequest;
    private LoginUserRequest loginRequest;
    private UpdateUserRequest userRequest;
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
            "Login this user and Success Get user`s email and name. " +
            "GET https://stellarburgers.nomoreparties.site/api/auth/user")
    public void getAuthUserInfoTest() {
        loginRequest = new LoginUserRequest(email, password);
        loginUser.loginUser(loginRequest);
        Response response = updateUser.getUserInfo(accessToken);
        statusCode.return200(response);
        updateUser.getAuthUserInfoReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Get user email and name without auth")
    @Description("Create user with random valid email, password and name parameters " +
            "and Failed Get user`s email and name without auth. " +
            "GET https://stellarburgers.nomoreparties.site/api/auth/user")
    public void getNotAuthUserInfoTest() {
        Response response = updateUser.getUserInfoWithoutToken();
        statusCode.return401(response);
        updateUser.getNotAuthUserInfoReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Update auth user email")
    @Description("Create user with random valid email, password and name parameters. " +
            "Login this user and Success Update user`s email. " +
            "PATCH https://stellarburgers.nomoreparties.site/api/auth/user")
    public void updateAuthUserEmailTest() {
        loginRequest = new LoginUserRequest(email, password);
        loginUser.loginUser(loginRequest);
        String newEmail = generateRandomString(3, 12) + DOMAINS[ThreadLocalRandom.current().nextInt(DOMAINS.length)];
        userRequest = new UpdateUserRequest(newEmail, null);
        Response response = updateUser.updateAuthUserInfo(userRequest, accessToken);
        statusCode.return200(response);
        updateUser.updateUserReturnCorrectBody(response, newEmail, name);
    }

    @Test
    @DisplayName("Update auth user name")
    @Description("Create user with random valid email, password and name parameters. " +
            "Login this user and Success Update user`s name. " +
            "PATCH https://stellarburgers.nomoreparties.site/api/auth/user")
    public void updateAuthUserNameTest() {
        loginRequest = new LoginUserRequest(email, password);
        loginUser.loginUser(loginRequest);
        String newName = generateRandomString(3, 12).toLowerCase();
        userRequest = new UpdateUserRequest(null, newName);
        Response response = updateUser.updateAuthUserInfo(userRequest, accessToken);
        statusCode.return200(response);
        updateUser.updateUserReturnCorrectBody(response, email, newName);
    }

    @Test
    @DisplayName("Update auth user with email that already been taken")
    @Description("Create two users with random valid email, password and name parameters. " +
            "Login first user and Failed Update first user`s email with second user email. " +
            "PATCH https://stellarburgers.nomoreparties.site/api/auth/user")
    public void updateAuthUserEmailThatAlreadyBeenTakenTest() {
        String oldUserEmail = generateRandomString(3, 12) + DOMAINS[ThreadLocalRandom.current().nextInt(DOMAINS.length)];
        String oldUserPassword = generateRandomString(3, 12).toLowerCase();
        String oldUserName = generateRandomString(3, 12).toLowerCase();
        createRequest = new CreateUserRequest(oldUserEmail, oldUserPassword, oldUserName);
        Response oldUserResponse = createUser.createUser(createRequest);
        String oldUserAccessToken = user.getAccessToken(oldUserResponse);
        loginRequest = new LoginUserRequest(email, password);
        loginUser.loginUser(loginRequest);
        userRequest = new UpdateUserRequest(oldUserEmail, null);
        Response response = updateUser.updateAuthUserInfo(userRequest, accessToken);
        statusCode.return403(response);
        updateUser.getAuthUserUpdateEmailThatAlreadyBeenTakenReturnCorrectBody(response);
        Response oldUserTokenResponse = user.deleteUser(oldUserAccessToken);
        Allure.step("Delete old user: " + oldUserTokenResponse.getBody().asString());
    }

    @Test
    @DisplayName("Update user email without auth")
    @Description("Create user with random valid email, password and name parameters " +
            "and Failed Update user`s email without auth. " +
            "PATCH https://stellarburgers.nomoreparties.site/api/auth/user")
    public void updateNotAuthUserEmailTest() {
        String newEmail = generateRandomString(3, 12) + DOMAINS[ThreadLocalRandom.current().nextInt(DOMAINS.length)];
        userRequest = new UpdateUserRequest(newEmail, null);
        Response response = updateUser.updateNotAuthUserInfoWithoutToken(userRequest);
        statusCode.return401(response);
        updateUser.getNotAuthUserInfoReturnCorrectBody(response);
    }

    @Test
    @DisplayName("Update user name without auth")
    @Description("Create user with random valid email, password and name parameters " +
            "and Failed Update user`s name without auth. " +
            "PATCH https://stellarburgers.nomoreparties.site/api/auth/user")
    public void updateNotAuthUserNameTest() {
        String newName = generateRandomString(3, 12).toLowerCase();
        userRequest = new UpdateUserRequest(null, newName);
        Response response = updateUser.updateNotAuthUserInfoWithoutToken(userRequest);
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
