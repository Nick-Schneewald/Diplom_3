package ru.yandex_praktikum.diplom_3.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex_praktikum.diplom_3.pojo.*;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {
    public static final String CREATE_USER_HANDLE = "/api/auth/register";
    public static final String LOGIN_USER_HANDLE = "/api/auth/login";
    public static final String UPDATE_USER_HANDLE = "/api/auth/user";

    public static final String DELETE_USER_HANDLE = "/api/auth/user";

    @Step("Создание пользователся со всеми обязательными параметрами")
    public ValidatableResponse create(CreateUserRequest createUserRequest) {
        return given()
                .spec(getSpec())
                .body(createUserRequest)
                .when()
                .post(CREATE_USER_HANDLE)
                .then();
    }

    @Step("Выполнение логина со всеми обязательными параметрами")
    public ValidatableResponse login(LoginUserRequest loginUserRequest) {
        return given()
                .spec(getSpec())
                .body(loginUserRequest)
                .when()
                .post(LOGIN_USER_HANDLE)
                .then();
    }

    @Step("Удаление пользователя")
    public void delete(String bearerToken) {
        given()
                .spec(getSpec())
                .auth().oauth2(bearerToken)
                .when()
                .delete(DELETE_USER_HANDLE);
    }
}
