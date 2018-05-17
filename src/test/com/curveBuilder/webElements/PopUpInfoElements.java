package com.curveBuilder.webElements;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PopUpInfoElements {

    private final WebDriver driver;

    private String popupWindow = "//div[@class = 'popup']";
    private By popUpWindowLocator = By.className("popup");

    private By popUpButtonLocator(String button) {
        return By.xpath(popupWindow + String.format("//button[contains(., '%s')]", button));
    }

    public PopUpInfoElements(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement popUpWindow() {
        return driver.findElement(popUpWindowLocator);
    }

    public Boolean isPopUpDisplayed() {
        try {
            driver.findElement(popUpWindowLocator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public WebElement popUpButton(String button) {
        return driver.findElement(popUpButtonLocator(button));
    }
}
