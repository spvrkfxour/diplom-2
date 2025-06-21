package ru.yandex.praktikum.env;


public class EnvConst {

    public static final String URL = "https://stellarburgers.nomoreparties.site/";

    public static final String CREATE_USER_ENDPOINT = "api/auth/register";
    public static final String LOGIN_USER_ENDPOINT = "api/auth/login";
    public static final String DELETE_USER_ENDPOINT = "api/auth/user";
    public static final String GET_USER_INFO_ENDPOINT = "api/auth/user";
    public static final String UPDATE_USER_ENDPOINT = "api/auth/user";
    public static final String GET_INGREDIENTS_ENDPOINT = "api/ingredients";
    public static final String CREATE_ORDER_ENDPOINT = "api/orders";

    public static final String[] DOMAINS = { "@gmail.com", "@yandex.ru", "@mail.ru" };

    public static final String CREATE_USER_ALREADY_EXISTS_MSG_ERROR = "User already exists";
    public static final String CREATE_USER_WITHOUT_PARAM_MSG_ERROR = "Email, password and name are required fields";
    public static final String LOGIN_USER_WITH_WRONG_PARAM_MSG_ERROR = "email or password are incorrect";
    public static final String GET_NOT_AUTH_USER_INFO_MSG_ERROR = "You should be authorised";
    public static final String UPDATE_USER_WITH_EMAIL_THAT_ALREADY_TAKEN_MSG_ERROR = "User with such email already exists";
    public static final String CREATE_EMPTY_ORDER_MSG_ERROR = "Ingredient ids must be provided";
}
