package ru.yandex_praktikum.diplom_3.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StellarBurgersRestorePasswordPage {
    private final WebDriver driver;
    private final By restorePasswordLabel = By.xpath("//h2[text()='Восстановление пароля']");

    private final By signInLink = By.xpath(".//a[text()='Войти']");
    public StellarBurgersRestorePasswordPage(WebDriver driver){
        this.driver = driver;
    }

    public void waitForLoadingRestorePasswordPage(){
        new WebDriverWait(driver,8)
                .until(ExpectedConditions.visibilityOfElementLocated(restorePasswordLabel));
    }

    public void signInLinkPerformClick(){
        driver.findElement(signInLink).click();
    }
}
