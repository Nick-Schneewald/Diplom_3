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
import ru.yandex_praktikum.diplom_3.dataproviders.UserProvider;
import ru.yandex_praktikum.diplom_3.pageobjects.StellarBurgersLoginPage;
import ru.yandex_praktikum.diplom_3.pageobjects.StellarBurgersMainPage;
import ru.yandex_praktikum.diplom_3.pageobjects.StellarBurgersRegisterPage;
import ru.yandex_praktikum.diplom_3.pageobjects.StellarBurgersRestorePasswordPage;
import ru.yandex_praktikum.diplom_3.pojo.CreateUserRequest;
import ru.yandex_praktikum.diplom_3.pojo.LoginUserRequest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LoginSeleniumChromeTest {
    private static final int BEARER_TOKEN_START_POS = 7;
    private static final String STELLAR_BURGERS_BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private static final String REGISTER_PAGE = "register";
    private static final String FORGOT_PASSWORD_PAGE = "forgot-password";
    private WebDriver driver;
    private StellarBurgersMainPage mainPage;
    private StellarBurgersRegisterPage registerPage;
    private StellarBurgersLoginPage loginPage;

    private StellarBurgersRestorePasswordPage restorePasswordPage;
    private LoginUserRequest loginUserRequest;

    private String bearerToken;
    private final UserClient userClient = new UserClient();

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
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        CreateUserRequest createUserRequest = UserProvider.getRandomCreateUserRequest(8);

        String accessToken = userClient.create(createUserRequest).extract().jsonPath().get("accessToken");
        bearerToken = accessToken.substring(BEARER_TOKEN_START_POS);
        loginUserRequest = LoginUserRequest.from(createUserRequest);
    }

    @Test
    @DisplayName("Выполнение логина с помощью кнопки 'Войти в аккаунт'")
    @Description("Проверка выполнения входа с помощью кнопки 'Войти в аккаунт' на главной странице")
    public void performLoginWithSignInButton() {
        driver.get(STELLAR_BURGERS_BASE_URL);
        mainPage = new StellarBurgersMainPage(driver);
        mainPage.waitForLoadingMainPage();
        mainPage.performClickSignInButton();

        loginPage = new StellarBurgersLoginPage(driver);
        loginPage.waitForLoadingLoginPage();
        loginPage.fillInAuthentificationData(loginUserRequest.getEmail(), loginUserRequest.getPassword());
        loginPage.proceedLoginData();

        mainPage.waitForLoadingMainPage();
        Assert.assertEquals(STELLAR_BURGERS_BASE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Выполнение логина с помощью кнопки 'Личный кабинет'")
    @Description("Проверка выполнения входа с помощью кнопки 'Личный кабинет'")
    public void performLoginWithPersonalAccountButton() {
        driver.get(STELLAR_BURGERS_BASE_URL);
        mainPage = new StellarBurgersMainPage(driver);
        mainPage.waitForLoadingMainPage();
        mainPage.performClickPersonalAccountButton();

        loginPage = new StellarBurgersLoginPage(driver);
        loginPage.waitForLoadingLoginPage();
        loginPage.fillInAuthentificationData(loginUserRequest.getEmail(), loginUserRequest.getPassword());
        loginPage.proceedLoginData();

        mainPage.waitForLoadingMainPage();
        Assert.assertEquals(STELLAR_BURGERS_BASE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Выполнение логина через кнопку в форме регистрации")
    @Description("Проверка выполнения входа с помощью кнопки в форме регистрации")
    public void performLoginFromRegistrationForm() {
        driver.get(STELLAR_BURGERS_BASE_URL + REGISTER_PAGE);
        registerPage = new StellarBurgersRegisterPage(driver);
        registerPage.waitForLoadingRegisterPage();

        registerPage.signInLinkPerformClick();

        loginPage = new StellarBurgersLoginPage(driver);
        loginPage.waitForLoadingLoginPage();
        loginPage.fillInAuthentificationData(loginUserRequest.getEmail(), loginUserRequest.getPassword());
        loginPage.proceedLoginData();

        mainPage = new StellarBurgersMainPage(driver);
        mainPage.waitForLoadingMainPage();
        Assert.assertEquals(STELLAR_BURGERS_BASE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Выполнение логина через форму восстановления пароля")
    @Description("Проверка выполнения входа с помощью кнопки в форме восстановления пароля")
    public void performLoginFromRestoreAccountForm() {
        driver.get(STELLAR_BURGERS_BASE_URL + FORGOT_PASSWORD_PAGE);
        restorePasswordPage = new StellarBurgersRestorePasswordPage(driver);
        restorePasswordPage.waitForLoadingRestorePasswordPage();

        restorePasswordPage.signInLinkPerformClick();

        loginPage = new StellarBurgersLoginPage(driver);
        loginPage.waitForLoadingLoginPage();
        loginPage.fillInAuthentificationData(loginUserRequest.getEmail(), loginUserRequest.getPassword());
        loginPage.proceedLoginData();

        mainPage = new StellarBurgersMainPage(driver);
        mainPage.waitForLoadingMainPage();
        Assert.assertEquals(STELLAR_BURGERS_BASE_URL, driver.getCurrentUrl());
    }

    @After
    public void tearDown() {
        if (bearerToken != null) {
            userClient.delete(bearerToken);
        }
        driver.close();
        driver.quit();
    }
}
