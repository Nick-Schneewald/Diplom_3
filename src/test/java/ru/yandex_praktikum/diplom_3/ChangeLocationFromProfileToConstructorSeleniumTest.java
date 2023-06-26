package ru.yandex_praktikum.diplom_3;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Cookie;
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

public class ChangeLocationFromProfileToConstructorSeleniumTest {
    private static final int BEARER_TOKEN_START_POS = 7;
    private static final String STELLAR_BURGERS_BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private static final String LOGIN_PAGE_URL = "login";
    private static final String PROFILE_PAGE_URL = "account/profile";
    private WebDriver driver;
    private String bearerToken;
    private UserClient userClient = new UserClient();
    private LoginUserRequest loginUserRequest;
    private StellarBurgersProfilePage profilePage;
    @Before
    public void setUp() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("application.properties"));
        String yandexBinaryPath = System.getProperty("yandex.binary.path");
        String yandexDriverPath = System.getProperty("yandex.driver.path");
        String browser = System.getProperty("browser");
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
    @DisplayName("Переход из личного кабинета в конструктор, используя лого")
    @Description("Проверка перехода в конструктор по клику на логотип Stellar Burgers")
    public void changeLocationFromProfileToHomePageUsingLogo(){
        driver.get(STELLAR_BURGERS_BASE_URL + LOGIN_PAGE_URL);
        StellarBurgersLoginPage loginPage = new StellarBurgersLoginPage(driver);
        loginPage.waitForLoadingLoginPage();
        loginPage.fillInAuthentificationData(loginUserRequest.getEmail(), loginUserRequest.getPassword());
        loginPage.proceedLoginData();

        StellarBurgersMainPage mainPage = new StellarBurgersMainPage(driver);
        mainPage.waitForLoadingMainPage();

        mainPage.performClickPersonalAccountButton();
        profilePage = new StellarBurgersProfilePage(driver);
        profilePage.waitForLoadingProfilePage();

        profilePage.clickHeaderLogo();
        profilePage.waitForLoadingProfilePage();
        Assert.assertEquals(STELLAR_BURGERS_BASE_URL,driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Переход из личного кабинета в конструктор, использвуя кнопку 'Конструктор'")
    @Description("Проверка перехода в конструктор по клику на кнопку 'Конструктор'")
    public void changeLocationFromProfileToHomePageUsingConstructorButton(){
        driver.get(STELLAR_BURGERS_BASE_URL + LOGIN_PAGE_URL);
        StellarBurgersLoginPage loginPage = new StellarBurgersLoginPage(driver);
        loginPage.waitForLoadingLoginPage();
        loginPage.fillInAuthentificationData(loginUserRequest.getEmail(), loginUserRequest.getPassword());
        loginPage.proceedLoginData();

        StellarBurgersMainPage mainPage = new StellarBurgersMainPage(driver);
        mainPage.waitForLoadingMainPage();

        mainPage.performClickPersonalAccountButton();
        profilePage = new StellarBurgersProfilePage(driver);
        profilePage.waitForLoadingProfilePage();

        profilePage.clickConstructorButton();
        profilePage.waitForLoadingProfilePage();
        Assert.assertEquals(STELLAR_BURGERS_BASE_URL, driver.getCurrentUrl() );
    }

    @After
    public void tearDown(){
        if (bearerToken != null) {
            userClient.delete(bearerToken);
        }
        driver.close();
        driver.quit();
    }
}
