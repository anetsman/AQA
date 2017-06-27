package com.curveBuilder.tenors;

import com.curveBuilder.utils.ObjectFactory;
import com.curveBuilder.utils.SwitchModes;
import com.curveBuilder.utils.WebDriverHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;

/**
 * Created by anetsman on 2017-06-06.
 */
public class Tenor extends ObjectFactory{
    private final WebDriverHelper webDriver;
    private String name;
    private TraderValue traderValue;
    private double marketValue;
    private TenorModel model;

    @FindBy(id = "IMPRButton")
    private WebElement improvedBtn;
    @FindBy(id = "userBtn")
    private WebElement userBtn;
    @FindBy(id = "settingsBtn")
    private WebElement settingsBtn;
    @FindBy(id = "traderValue")
    private ArrayList<WebElement> traderValueField;
    @FindBy(id = "marketValue")
    private WebElement marketValueField;
    @FindBy(id = "accept")
    private  WebElement acceptBtn;
    @FindBy(id = "decline")
    private  WebElement declineBtn;

    public Tenor(WebElement tenor, WebDriverHelper webDriverHelper) {
        super(webDriverHelper);
        this.webDriver = webDriverHelper;
        this.name = tenor.getAttribute("name");
        this.model = model;
    }

    public TraderValue getTraderValue() {
        return traderValue;
    }

    public void setTraderValue(TraderValue traderValue, boolean accept) {
        if (accept) {
            traderValueField.forEach(webElement ->{
                if (traderValue.getDriverName() != null) {
                    webDriver.fillInField(webElement, traderValue.getDriverName());
                }
                webDriver.fillInField(webElement, traderValue.getValue());
            });
            acceptBtn.click();
            this.traderValue = traderValue;
        }else declineBtn.click();
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        webDriver.fillInField(marketValueField, marketValue);
        this.marketValue = marketValue;
    }

    public String getName() {
        return name;
    }

    public void switchMarket(SwitchModes mode) {
        webDriver.switcher(improvedBtn, mode);
    }

    public void switchUser(SwitchModes mode) {
        webDriver.switcher(userBtn, mode);
    }

    public TenorSettings openSettings() {
        settingsBtn.click();
        return new TenorSettings(webDriver, this);
    }

    private class TraderValue {
        private String DriverName;
        private double Value;

        public TraderValue(String driverName, double value) {
            DriverName = driverName;
            Value = value;
        }

        public TraderValue(double value) {
            Value = value;
        }

        String getDriverName() {
            return DriverName;
        }

        double getValue() {
            return Value;
        }
    }

}
