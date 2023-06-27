package ru.yandex_praktikum.diplom_3.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StellarBurgersRegisterPage {
    private final WebDriver driver;

    private final By registerLabel = By.xpath(".//*[text()='Регистрация']");

    private final By userNameInput = By.xpath(".//label[text()='Имя']/following-sibling::*");

    private final By userEmailInput = By.xpath(".//label[text()='Email']/following-sibling::*");

    private final By userPasswordInput = By.xpath(".//input[@name='Пароль']");

    private final By registerButton = By.xpath(".//button[text()='Зарегистрироваться']");

    private final By badPasswordError = By.xpath(".//p[@class='input__error text_type_main-default']");

    private final By signInLink = By.xpath(".//a[text()='Войти']");
    public StellarBurgersRegisterPage(WebDriver driver){
        this.driver = driver;
    }

    public void waitForLoadingRegisterPage() {
        new WebDriverWait(driver,8)
                .until(ExpectedConditions.visibilityOfElementLocated(registerLabel));
    }

    public void fillInAuthentificationData(String name, String email, String password){
        driver.findElement(userNameInput).clear();
        driver.findElement(userNameInput).sendKeys(name);
        driver.findElement(userEmailInput).clear();
        driver.findElement(userEmailInput).sendKeys(email);
        driver.findElement(userPasswordInput).clear();
        driver.findElement(userPasswordInput).sendKeys(password);
    }

    public boolean isErrorMessageShown(){
        return driver.findElement(badPasswordError).isDisplayed();
    }

    public void proceedTheRegistration(){
        driver.findElement(registerButton).click();
    }

    public void signInLinkPerformClick(){
        driver.findElement(signInLink).click();
    }
}

