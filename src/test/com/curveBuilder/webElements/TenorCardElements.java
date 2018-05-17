package com.curveBuilder.webElements;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.windowObjects.BrokerButton;
import com.curveBuilder.windowObjects.Tenor;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TenorCardElements {

    private final WebElement tenorElement;

    /**
     * Open card button
     */
    private By openCardButtonLocator = By.xpath(".//button[contains(@class, 'card-toggleBtn')]");

    /**
     * Benchmark and FitFlags
     */
    private By tenorCardLocator = By.xpath(".//div[@class = 'card']//div[contains(@class, 'card-content') ]");
    private By notActiveBenchmarkButtonLocator = By.xpath(".//label[contains(@class, 'rounded-btn')]");
    private By activeBenchmarkButtonLocator = By.xpath(".//label[contains(@class, 'benchmark-btn rounded-btn--active')]");
    private By fitFlagList = By.xpath("//ul[contains(@class, 'instruments-fitting')]//button");

    /**
     * Change Tenor drivers and rate section
     */
    private By rateInputField = By.xpath(".//input[@id = 'value']");
    private By codiFirstWingInputField = By.xpath(".//dropdown[@ng-model='ce.model.driver1Tenor']/input");
    private By codiSecondWingInputField = By.xpath(".//dropdown[@ng-model='ce.model.driver2Tenor']/input");
    private By flyFirstWingInputField = By.xpath(".//dropdown[@ng-model='fe.model.wing1Tenor']/input");
    private By flySecondWingInputField = By.xpath(".//dropdown[@ng-model='fe.model.wing2Tenor']/input");
    private By sprdDriverInputField = By.xpath(".//dropdown[@ng-model='se.model.source']/input");

    private By driverSelector(String driverName) {
        return By.xpath(String.format(".//li[contains(., '%s')]", driverName));
    }
    private By applyButton = By.xpath(".//button[contains(., 'Apply')]");
    private By cancelButton = By.xpath(".//button[contains(., 'Cancel')]");

    /**
     * Brokers section
     */
    private By allBrokers = By.xpath("//broker-prices//label");
    private String brokerNameLocator = ".//label[contains(., '%s')]";

    private By brokerCheckBoxLocator(String brokerName) {
        return By.xpath(String.format(brokerNameLocator + "/../input", brokerName.toUpperCase()));
    }

    private By brokerElementLocator(String brokerName) {
        return By.xpath(String.format(brokerNameLocator, brokerName.toUpperCase()));
    }

    private By brokerTimestamp(String brokerName) {
        return By.xpath(String.format(brokerNameLocator + "/../../span[contains(@class, 'fulline')]", brokerName));
    }

    private By brokerRatesList(String brokerName) {
        return By.xpath(String.format(brokerNameLocator + "/../..//span[contains(@class, 'value')]", brokerName));
    }

    /**
     * Constructor
     * @param tenor
     */
    public TenorCardElements(Tenor tenor) {
        this.tenorElement = tenor.getTenorElement();
    }

    public WebElement openCardButton() {
        return tenorElement.findElement(openCardButtonLocator);
    }

    public WebElement benchmarkButton() {
        return tenorElement.findElement(notActiveBenchmarkButtonLocator);
    }

    public WebElement benchmarkButtonActive() {
        return tenorElement.findElement(activeBenchmarkButtonLocator);
    }

    public List<WebElement> fitFlags() {
        return tenorElement.findElements(fitFlagList);
    }

    public WebElement tenorCard() {
        try {
            return tenorElement.findElement(tenorCardLocator);
        } catch (NoSuchElementException e) {
//            e.printStackTrace();
            return null;
        }
    }

    public WebElement codiFirstWingInput() {
        return tenorElement.findElement(codiFirstWingInputField);
    }

    public WebElement codiSecondWingInput() {
        return tenorElement.findElement(codiSecondWingInputField);
    }

    public WebElement flyFirstWingInput() {
        return tenorElement.findElement(flyFirstWingInputField);
    }

    public WebElement flySecondWingInput() {
        return tenorElement.findElement(flySecondWingInputField);
    }

    public WebElement driverInputField() {
        return tenorElement.findElement(sprdDriverInputField);
    }

    public WebElement selectDriverForTenor(String driverName) {
        return tenorElement.findElement(driverSelector(driverName));
    }

    public WebElement rateInputField() {
        return tenorCard().findElement(rateInputField);
    }

    public WebElement applyButton() {
        return tenorCard().findElement(applyButton);
    }

    public WebElement cancelButton() {
        return tenorCard().findElement(cancelButton);
    }

    /**
     * @param brokerName
     * @return clickable element for selenium
     */
    public BrokerButton brokerCheckBox(String brokerName) {
        try {
            boolean checked = brokerCheckBoxWithState(brokerName).getAttribute("class").contains("not-empty");
            if (checked) {
                return new BrokerButton(tenorElement.findElement(brokerElementLocator(brokerName)), SwitchStates.ON);
            }
        } catch (Exception e) {

        }
        return new BrokerButton(tenorElement.findElement(brokerElementLocator(brokerName)), SwitchStates.OFF);
    }

    /**
     * @param brokerName
     * @return checkbox for checking if it is clicked
     */
    public WebElement brokerCheckBoxWithState(String brokerName) {
        return tenorElement.findElement(brokerCheckBoxLocator(brokerName));
    }

    public WebElement brokerTimeStamp(String brokerName) {
        return tenorElement.findElement(brokerTimestamp(brokerName));
    }

    public List<WebElement> brokerRates(String brokerName) {
        return tenorElement.findElements(brokerRatesList(brokerName));
    }

    public List<WebElement> allBrokers() {
        return tenorElement.findElements(allBrokers);
    }
}
