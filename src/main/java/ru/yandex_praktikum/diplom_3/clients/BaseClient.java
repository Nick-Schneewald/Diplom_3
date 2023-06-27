package ru.yandex_praktikum.diplom_3.clients;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseClient {
    private static final String STELLAR_BURGERS_BASE_URI = "https://stellarburgers.nomoreparties.site";

    @Step("Метод возвращающий базовую спецификацию для всех запросов")
    protected RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(STELLAR_BURGERS_BASE_URI)
                .build();
    }
}
