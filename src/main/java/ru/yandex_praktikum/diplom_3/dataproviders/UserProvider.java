package ru.yandex_praktikum.diplom_3.dataproviders;

import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex_praktikum.diplom_3.pojo.*;

public class UserProvider {
    public static final String BASE_EMAIL_DOMAIN = "@yandex.ru";

    public static CreateUserRequest getRandomCreateUserRequest(int defaultLen) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(RandomStringUtils.randomAlphabetic(defaultLen) + BASE_EMAIL_DOMAIN);
        createUserRequest.setPassword(RandomStringUtils.randomAlphabetic(defaultLen));
        createUserRequest.setName(RandomStringUtils.randomAlphabetic(defaultLen));
        return createUserRequest;
    }
}
