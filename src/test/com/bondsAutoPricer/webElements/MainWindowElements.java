package com.bondsAutoPricer.webElements;

import org.openqa.selenium.By;

public class MainWindowElements {

    public static By waitingForConnectionPopUpLocator = By.xpath("//span[contains(., 'Connecting to the server')]");

    /**
     * Bonds Selectors
     */
    private String  headerSelector = "//label[contains(@class, 'header-selector')]";
    private By headerDropDownButton = By.xpath(headerSelector + "//button");

    public By bondSelector(String bondName) {
        return By.xpath(headerSelector + String.format("li[contains(., '%s')]", bondName));
    }

    public MainWindowElements() {
    }

    public By getHeaderDropDownButton() {
        return headerDropDownButton;
    }
}
