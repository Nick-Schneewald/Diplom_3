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
import ru.yandex_praktikum.diplom_3.pageobjects.StellarBurgersMainPage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SwitchTabsInBurgerConstructorSeleniumTest {
    private static final String STELLAR_BURGERS_BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private WebDriver driver;

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
    }

    @Test
    @DisplayName("Переход с закладки по-умолчанию на закладку 'Соусы'")
    @Description("Проверка перехода к разделу 'Соусы'")
    public void switchCustomConstructorTabToSaucesTab(){
        driver.get(STELLAR_BURGERS_BASE_URL);
        StellarBurgersMainPage mainPage = new StellarBurgersMainPage(driver);
        mainPage.waitForLoadingMainPage();

        mainPage.performClickSaucesTab();
        Assert.assertEquals("tab_tab__1SPyG tab_tab_type_current__2BEPc pt-4 pr-10 pb-4 pl-10 noselect",mainPage.getCurrentClassValueSauces());
    }

    @Test
    @DisplayName("Переход с закладки по-умолчанию на закладку 'Начинки'")
    @Description("Проврка перехода к разделу 'Начинки'")
    public void switchCustomConstructorTabToFillingsTab(){
        driver.get(STELLAR_BURGERS_BASE_URL);
        StellarBurgersMainPage mainPage = new StellarBurgersMainPage(driver);
        mainPage.waitForLoadingMainPage();

        mainPage.performClickFillingsTab();

        Assert.assertEquals("tab_tab__1SPyG tab_tab_type_current__2BEPc pt-4 pr-10 pb-4 pl-10 noselect",mainPage.getCurrentClassValueFillings());
    }

    @Test
    @DisplayName("Переход с закладки по-умолчанию на закладку 'Булки'")
    @Description("Проверка перхода к разделу 'Булки'")
    public void switchCustomConstructorTabToBunsTab(){
        driver.get(STELLAR_BURGERS_BASE_URL);
        StellarBurgersMainPage mainPage = new StellarBurgersMainPage(driver);
        mainPage.waitForLoadingMainPage();

        mainPage.performClickFillingsTab();
        mainPage.waitForLoadingSaucesTab();
        mainPage.performClickBunsTab();
        Assert.assertEquals("tab_tab__1SPyG tab_tab_type_current__2BEPc pt-4 pr-10 pb-4 pl-10 noselect",mainPage.getCurrentClassValueBuns());
    }
    @After
    public void tearDown(){
        driver.close();
        driver.quit();
    }
}
