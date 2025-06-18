package ru.yandex.praktikum.env;


public class EnvConst {

    public static final String URL = "https://stellarburgers.nomoreparties.site/";

    public static final String CREATE_USER_ENDPOINT = "api/auth/register";
    public static final String LOGIN_USER_ENDPOINT = "api/auth/login";
    public static final String DELETE_USER_ENDPOINT = "api/auth/user";

    public static final String[] DOMAINS = { "@gmail.com", "@yandex.ru", "@mail.ru" };

    public static final String CREATE_USER_ALREADY_EXISTS_MSG_ERROR = "User already exists";
    public static final String CREATE_USER_WITHOUT_PARAM_MSG_ERROR = "Email, password and name are required fields";
}
