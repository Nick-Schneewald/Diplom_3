package ru.yandex_praktikum.diplom_3.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StellarBurgersMainPage {
    private final WebDriver driver;
    private  final By signInButton = By.xpath(".//button[text()='Войти в аккаунт']");
    private final By personalAccountButton = By.xpath(".//*[@href='/account']");

    private final By bunsTab = By.xpath(".//span[text()='Булки']/parent::*");
    private final By saucesTab = By.xpath(".//span[text()='Соусы']/parent::div");
    private final By fillingsTab = By.xpath(".//span[text()='Начинки']/parent::*");
    private final By createBurgerLabel = By.xpath(".//h1[text()='Соберите бургер']");
    public StellarBurgersMainPage(WebDriver driver){
        this.driver = driver;
    }

    public void waitForLoadingMainPage(){
        new WebDriverWait(driver,8)
                .until(ExpectedConditions.visibilityOfElementLocated(createBurgerLabel));
    }

    public void waitForLoadingSaucesTab(){
        new WebDriverWait(driver,8)
                .until(ExpectedConditions.elementToBeClickable(bunsTab));
    }
    public void performClickSignInButton(){
        driver.findElement(signInButton).click();
    }

    public void performClickPersonalAccountButton(){
        driver.findElement(personalAccountButton).click();
    }

    public void performClickBunsTab(){driver.findElement(bunsTab).click();}
    public void performClickSaucesTab(){driver.findElement(saucesTab).click();}
    public void performClickFillingsTab(){driver.findElement(fillingsTab).click();}

    public String getCurrentClassValueBuns(){return driver.findElement(bunsTab).getAttribute("class");}
    public String getCurrentClassValueSauces(){return driver.findElement(saucesTab).getAttribute("class");}
    public String getCurrentClassValueFillings(){return driver.findElement(fillingsTab).getAttribute("class");}

}
