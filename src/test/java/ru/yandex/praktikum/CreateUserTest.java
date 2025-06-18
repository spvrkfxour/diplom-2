package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.dto.CreateUserRequest;
import ru.yandex.praktikum.steps.CreateUserSteps;
import ru.yandex.praktikum.steps.StatusCodeSteps;

import java.util.concurrent.ThreadLocalRandom;

import static ru.yandex.praktikum.env.EnvConst.*;


public class CreateUserTest {
    private final CreateUserSteps createUser = new CreateUserSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private CreateUserRequest request;
    private String email;
    private String password;
    private String name;

    @Before
    public void setUp() {
        email = RandomStringUtils.randomAlphanumeric(3, 12).toLowerCase() +
                DOMAINS[ThreadLocalRandom.current().nextInt(DOMAINS.length)];
        password = RandomStringUtils.randomAlphanumeric(3, 12).toLowerCase();
        name = RandomStringUtils.randomAlphanumeric(3, 12).toLowerCase();
        request = new CreateUserRequest(email, password, name);
    }

    @Test
    @DisplayName("Create user with random valid parameters")
    @Description("Success create user with random valid email, password and name parameters. " +
            "POST https://stellarburgers.nomoreparties.site/api/auth/register")
    public void createValidUserTest() {
        Response response = createUser.createUserStepTest(request);
        statusCode.return200Test(response);
        createUser.createUserReturnCorrectBodyTest(response);
    }
}
