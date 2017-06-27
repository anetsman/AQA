package com.curveBuilder.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by anetsman on 2017-06-06.
 */
public class WebDriverHelper {
    private final WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void switcher(WebElement element, SwitchModes mode) {
        boolean turnedOn = false;
        //TODO try catch for wide using - check for different attributes
        if (element.getAttribute("checked").equals("checked")) {
            turnedOn = true;
        }

        if (mode == SwitchModes.ON) {
            if (!turnedOn) {
                element.click();
            }
        } else {
            if (turnedOn) {
                element.click();
            }
        }
    }

    public void fillInField(WebElement field, String text) {
        field.clear();
        field.sendKeys(text);
    }
    public void fillInField(WebElement field, double value) {
        field.clear();
        field.sendKeys(String.valueOf(value));
    }
}
