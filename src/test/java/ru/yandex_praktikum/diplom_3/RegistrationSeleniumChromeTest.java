package ru.yandex_praktikum.diplom_3;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.yandex_praktikum.diplom_3.clients.UserClient;
import ru.yandex_praktikum.diplom_3.pojo.CreateUserRequest;
import ru.yandex_praktikum.diplom_3.dataproviders.UserProvider;
import ru.yandex_praktikum.diplom_3.pageobjects.StellarBurgersLoginPage;
import ru.yandex_praktikum.diplom_3.pageobjects.StellarBurgersRegisterPage;
import ru.yandex_praktikum.diplom_3.pojo.LoginUserRequest;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RegistrationSeleniumChromeTest {
    private static final String STELLAR_BURGERS_BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private static final String REGISTER_PAGE = "register";
    private static final String LOGIN_PAGE = "login";
    private static final int BEARER_TOKEN_START_POS = 7;
    private WebDriver driver;
    private StellarBurgersRegisterPage registerPage;
    private CreateUserRequest createUserRequest;

    @Before
    public void setUp() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("application.properties"));
        String yandexBinaryPath = System.getProperty("yandex.binary.path");
        String yandexDriverPath = System.getProperty("yandex.driver.path");
        String browser=System.getProperty("browser");
        switch(browser) {
            case "chrome":{
                driver = new ChromeDriver();
                break;
            }
            case "yandex": {
                System.setProperty("webdriver.chrome.driver", yandexDriverPath);
                ChromeOptions options = new ChromeOptions();
                options.setBinary(yandexBinaryPath);
                driver = new ChromeDriver(options);
                break;
            }
        }
        //driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
    }
    @Test
    @DisplayName("Пользователь должен быть зарегестрирован с паролем 6 символов")
    @Description("Проверка граничного значения для пароля, ожидается успешная регистрация")
    public void userShouldBeRegisteredWith6CharsPass(){
        createUserRequest = UserProvider.getRandomCreateUserRequest(6);

        driver.get(STELLAR_BURGERS_BASE_URL + REGISTER_PAGE);
        registerPage = new StellarBurgersRegisterPage(driver);

        registerPage.waitForLoadingRegisterPage();
        registerPage.fillInAuthentificationData(
                createUserRequest.getName(),
                createUserRequest.getEmail(),
                createUserRequest.getPassword());

        registerPage.proceedTheRegistration();
        StellarBurgersLoginPage loginPage = new StellarBurgersLoginPage(driver);
        loginPage.waitForLoadingLoginPage();
        Assert.assertEquals(STELLAR_BURGERS_BASE_URL + LOGIN_PAGE,driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Пользователь не должен быть зарегестрирован с паролем меньше 6 символов")
    @Description("Проверка случая шага за границу разрешенного диапазона [6; +inf.) для пароля, ожидается сообщение об ошибке")
    public void userShouldNotBeRegisteredWithLessThen6CharPass(){
        createUserRequest = UserProvider.getRandomCreateUserRequest(5);

        driver.get(STELLAR_BURGERS_BASE_URL + REGISTER_PAGE);
        registerPage = new StellarBurgersRegisterPage(driver);

        registerPage.waitForLoadingRegisterPage();
        registerPage.fillInAuthentificationData(
                createUserRequest.getName(),
                createUserRequest.getEmail(),
                createUserRequest.getPassword());

        registerPage.proceedTheRegistration();
        Assert.assertTrue(registerPage.isErrorMessageShown());
        Assert.assertEquals(STELLAR_BURGERS_BASE_URL + REGISTER_PAGE,driver.getCurrentUrl());
    }


    @Test
    @DisplayName("Пользователь должен быть зарегестрирован с паролем больше 6 символов")
    @Description("Проверка случая шага внутрь границы разрешенного диапазона [6; +inf.) для пароля, ожидается успешная регистрация")
    public void userShouldBeRegisteredWithMoreThen6CharPass(){
        createUserRequest = UserProvider.getRandomCreateUserRequest(7);

        driver.get(STELLAR_BURGERS_BASE_URL + REGISTER_PAGE);
        registerPage = new StellarBurgersRegisterPage(driver);

        registerPage.waitForLoadingRegisterPage();
        registerPage.fillInAuthentificationData(
                createUserRequest.getName(),
                createUserRequest.getEmail(),
                createUserRequest.getPassword());

        registerPage.proceedTheRegistration();
        StellarBurgersLoginPage loginPage = new StellarBurgersLoginPage(driver);
        loginPage.waitForLoadingLoginPage();
        Assert.assertEquals(STELLAR_BURGERS_BASE_URL + LOGIN_PAGE,driver.getCurrentUrl());
    }
    @After
    public void tearDown(){
        UserClient userClient = new UserClient();
        LoginUserRequest loginUserRequest = LoginUserRequest.from(createUserRequest);
        /*Если убрать здесь проверку, то метод с аннотацией @After сломается после теста userShouldNotBeRegisteredWithLessThen6CharPass()
        Когда проверяется попытка регистрации с слишком коротким паролем и userClient.login() не вернет токен accessToken,
        соотвественно и bearerToken*/
        if(userClient.login(loginUserRequest).extract().jsonPath().get("success").equals("true")){
            String accessToken = userClient.login(loginUserRequest).extract().jsonPath().get("accessToken");
            String bearerToken = accessToken.substring(BEARER_TOKEN_START_POS);
            if (bearerToken != null) {
                userClient.delete(bearerToken);
            }
        }
        driver.close();
        driver.quit();
    }
}
