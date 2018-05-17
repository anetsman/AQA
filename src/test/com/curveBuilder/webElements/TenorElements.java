package com.curveBuilder.webElements;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.windowObjects.HouseButton;
import com.curveBuilder.windowObjects.MarketButton;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TenorElements {
    private  WebElement tenorElement;

    private By benchmark = By.xpath(".//div[contains(@class, 'main ng-scope')]");

    /**
     * Enabled buttons
     */
    private String tenorsButtonEnabled = ".//button[@class = 'default-btn']";
    private By marketButtonEnabled = By.xpath(tenorsButtonEnabled + "//span[contains(., 'IMPR')]/../..");
    private By houseButtonEnabled = By.xpath(tenorsButtonEnabled + "/span/..");

    /**
     * Disabled buttons
     */
    private String tenorsButtonDisabled = ".//button[@class = 'default-btn is-part-disabled']";
    private By marketButtonDisabled = By.xpath(tenorsButtonDisabled + "//time[contains(., '--')]/../div/..");
    private By houseButtonDisabled = By.xpath(tenorsButtonDisabled + "/span/..");

    /**
     * Ready buttons
     */
    private String tenorsButtonReady = ".//button[@class = 'default-btn is-ready']";
    private By marketButtonReady = By.xpath(tenorsButtonReady + "/div/..");
    private By houseButtonReady = By.xpath(tenorsButtonReady + "/span/..");

    /**
     * Pending buttons
     */
    private String tenorsButtonPending = ".//button[@class = 'default-btn is-warning']";
    private By marketButtonPending = By.xpath(tenorsButtonPending + "//time[contains(., '--')]/../div/..");
    private By houseButtonPending = By.xpath(tenorsButtonPending + "/span/..");

    /**
     * FitFlag
     */
    private By fitFlag = By.xpath(".//span[contains(@class, 'fit-flag')]");

    /**
     * House Model
     */
    private By houseModel = By.xpath("./span");
    private String modelSwitcher = ".//dropdown[contains(@change, 'selectModel')]";
    private By modelDropDownButton = By.xpath(modelSwitcher + "/button");
    private String modelSelectorLocator = modelSwitcher + "//ul/li[contains(., '%s')]";

    private By modelSelectorLocator(String model) {
        return By.xpath(String.format(modelSelectorLocator, model));
    }

    /**
     * Curve, House, Market values, Most powerful broker
     */
    private By marketValueReal = By.xpath(".//span[@class ='market-value']/span");
    private By marketValueImplied = By.xpath(".//span[@class ='market-value is-disabled']/span");
    private String curveValueSelector = ".//span[@class = 'curve-value%s']/span";
    private By curveValue = By.xpath(String.format(curveValueSelector, ""));
    private By curveValueImplied = By.xpath(String.format(curveValueSelector, " implied"));
    private By curveValueMismatch = By.xpath(String .format(curveValueSelector, " mismatch"));
    private String inlineRateFieldSelector = ".//div[@class='marker-wrapper ng-scope%s']//input[contains(@class, 'inline-rate')]";
    private By houseValueField = By.xpath(String.format(inlineRateFieldSelector, ""));
    private By houseValueReadyField = By.xpath(String.format(inlineRateFieldSelector, " user-model-ready"));
    private By mostPowerfulBroker = By.xpath(".//div[contains(@class, 'most-impactfull-broker')]/div");

    /**
     * Inline actions buttons
     */
    private By subtractButton = By.xpath(".//button[contains(@class, 'subt__btn')]");
    private By addButton = By.xpath(".//button[contains(@class, 'add__btn')]");
    private By approveButton = By.xpath(".//button[contains(@class, 'approve__btn')]");
    private By cancelButton = By.xpath(".//button[contains(@class, 'cancel__btn')]");

    public TenorElements(WebElement elementTenor) {
        this.tenorElement = elementTenor;
    }

    public Boolean isBenchmarked() {
        try {
            tenorElement.findElement(benchmark);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public MarketButton marketButton() {
        MarketButton marketButton;
        try {
            marketButton = new MarketButton(tenorElement.findElement(marketButtonEnabled), SwitchStates.ON);
        } catch (NoSuchElementException e) {
            try {
                marketButton = new MarketButton(tenorElement.findElement(marketButtonDisabled), SwitchStates.OFF);
            } catch (NoSuchElementException er) {
                try {
                    marketButton = new MarketButton(tenorElement.findElement(marketButtonReady), SwitchStates.READY);
                } catch (NoSuchElementException ex) {
                    marketButton = new MarketButton(tenorElement.findElement(marketButtonPending), SwitchStates.PENDING);
                }
            }
        }
        return marketButton;
    }

    public HouseButton houseButton() {
        HouseButton houseButton;
        try {
            houseButton = new HouseButton(tenorElement.findElement(houseButtonEnabled), SwitchStates.ON);
        } catch (NoSuchElementException e) {
            try {
                houseButton = new HouseButton(tenorElement.findElement(houseButtonDisabled), SwitchStates.OFF);
            } catch (NoSuchElementException ex) {
                try {
                    houseButton = new HouseButton(tenorElement.findElement(houseButtonReady), SwitchStates.READY);
                } catch (NoSuchElementException er) {
                    houseButton = new HouseButton(tenorElement.findElement(houseButtonPending), SwitchStates.PENDING);
                }
            }
        }
        return houseButton;
    }

    public WebElement modelDropDownButton() {
        return tenorElement.findElement(modelDropDownButton);
    }

    public WebElement modelSelector(String modelName) {
        return tenorElement.findElement(modelSelectorLocator(modelName));
    }

    public WebElement marketValueReal() throws NoSuchElementException{
        return tenorElement.findElement(marketValueReal);
    }

    public WebElement marketValueImplied() throws NoSuchElementException{
        return tenorElement.findElement(marketValueImplied);
    }

    public WebElement curveValueReal() throws NoSuchElementException{
        return tenorElement.findElement(curveValue);
    }

    public WebElement curveValueImplied() throws NoSuchElementException {
        return tenorElement.findElement(curveValueImplied);
    }

    public WebElement getCurveValueMismatch() {
        return tenorElement.findElement(curveValueMismatch);
    }

    public WebElement houseValueField() throws NoSuchElementException{
        return tenorElement.findElement(houseValueField);
    }

    public WebElement houseValueReadyField() throws NoSuchElementException{
        return tenorElement.findElement(houseValueReadyField);
    }

    public List<WebElement> mostPowerfulBroker() {
        return tenorElement.findElements(mostPowerfulBroker);
    }

    public WebElement fitFlag() {
        return tenorElement.findElement(fitFlag);
    }

    public WebElement subtractButton() {
        return tenorElement.findElement(subtractButton);
    }

    public WebElement addButton() {
        return tenorElement.findElement(addButton);
    }

    public WebElement approveButton() {
        return tenorElement.findElement(approveButton);
    }

    public WebElement cancelButton() {
        return tenorElement.findElement(cancelButton);
    }

    public String getHouseModel() {
        return houseButton().getButtonElement().findElement(houseModel).getText();
    }
}

