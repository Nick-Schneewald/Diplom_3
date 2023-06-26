package ru.yandex_praktikum.diplom_3.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StellarBurgersLoginPage {
    private final WebDriver driver;
    private final By loginLabel = By.xpath(".//h2[text()='Вход']");
    private final By emailInput = By.xpath(".//label[text()='Email']/following-sibling::*");
    private final By passwordInput = By.xpath(".//input[@name='Пароль']");
    private final By signInButton = By.xpath(".//button[text()='Войти']");

    public StellarBurgersLoginPage(WebDriver driver){
        this.driver = driver;
    }

    public void waitForLoadingLoginPage(){
        new WebDriverWait(driver,8)
                .until(ExpectedConditions.visibilityOfElementLocated(loginLabel));
    }

    public void fillInAuthentificationData(String email,String password){
        driver.findElement(emailInput).clear();
        driver.findElement(emailInput).sendKeys(email);
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void proceedLoginData(){
        driver.findElement(signInButton).click();
    }
}
