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
import ru.yandex_praktikum.diplom_3.pageobjects.StellarBurgersProfilePage;
import ru.yandex_praktikum.diplom_3.pojo.CreateUserRequest;
import ru.yandex_praktikum.diplom_3.pojo.LoginUserRequest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
public class ChangeLocationFromHomeToProfileSeleniumTest {
    private static final int BEARER_TOKEN_START_POS = 7;
    private static final String STELLAR_BURGERS_BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private static final String LOGIN_PAGE_URL = "login";
    private static final String PROFILE_PAGE_URL = "account/profile";
    private WebDriver driver;
    private UserClient userClient = new UserClient();
    private LoginUserRequest loginUserRequest;
    private String bearerToken;

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
    @DisplayName("Переход в личный кабинет без токена на предъявителя")
    @Description("Проверка перехода по клику на 'Личный кабинет' не авторизованным пользоваьелем ")
    public void changeLocationToUserProfileWithoutBearerToken(){
        driver.get(STELLAR_BURGERS_BASE_URL);
        StellarBurgersMainPage mainPage = new StellarBurgersMainPage(driver);
        mainPage.waitForLoadingMainPage();

        mainPage.performClickPersonalAccountButton();
        StellarBurgersLoginPage loginPage = new StellarBurgersLoginPage(driver);
        loginPage.waitForLoadingLoginPage();

        Assert.assertEquals(STELLAR_BURGERS_BASE_URL + LOGIN_PAGE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Переход в личный кабинет с токеном на предъявителя")
    @Description("Проверка перехода по клику на 'Личный кабинет' авторизованным пользоваьелем ")
    public void changeLocationToUserProfileWithBearerToken(){
        driver.get(STELLAR_BURGERS_BASE_URL + LOGIN_PAGE_URL);
        StellarBurgersLoginPage loginPage = new StellarBurgersLoginPage(driver);
        loginPage.waitForLoadingLoginPage();
        loginPage.fillInAuthentificationData(loginUserRequest.getEmail(), loginUserRequest.getPassword());
        loginPage.proceedLoginData();

        StellarBurgersMainPage mainPage = new StellarBurgersMainPage(driver);
        mainPage.waitForLoadingMainPage();

        mainPage.performClickPersonalAccountButton();
        StellarBurgersProfilePage profilePage = new StellarBurgersProfilePage(driver);
        profilePage.waitForLoadingProfilePage();
        Assert.assertEquals(STELLAR_BURGERS_BASE_URL + PROFILE_PAGE_URL,driver.getCurrentUrl());
    }
    @After
    public void tearDown(){
        if(bearerToken != null) {
            userClient.delete(bearerToken);
        }
        driver.close();
        driver.quit();
    }
}



