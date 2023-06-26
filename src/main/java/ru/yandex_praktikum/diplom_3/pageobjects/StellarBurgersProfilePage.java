package ru.yandex_praktikum.diplom_3.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StellarBurgersProfilePage {
    private WebDriver driver;
    private By loadingSpinner = By.xpath(".//*[@alt='loading animation']");
    private By headerLogo = By.xpath(".//div[@class='AppHeader_header__logo__2D0X2']/child::*");
    private By constructorButton = By.xpath(".//p[text()='Конструктор']");
    private By logoutButton = By.xpath(".//button[text()='Выход']");
    public StellarBurgersProfilePage(WebDriver driver){
        this.driver = driver;
    }

    public void waitForLoadingProfilePage(){
        new WebDriverWait(driver,8)
                .until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
    }

    public void clickHeaderLogo(){
        driver.findElement(headerLogo).click();
    }

    public void clickConstructorButton(){
        driver.findElement(constructorButton).click();
    }

    public void clickLogoutButton() { driver.findElement(logoutButton).click();}

}
