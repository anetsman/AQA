package com.curveBuilder.windowObjects;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.enums.FitFlags;
import com.curveBuilder.enums.TenorModels;
import com.curveBuilder.utils.WebDriverHelper;
import com.curveBuilder.webElements.TenorCardElements;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by anetsman on 2017-06-06.
 */
public class TenorCard {
    private final static Logger log = Logger.getLogger(TenorCard.class);
    private final TenorCardElements tenorCardElements;

    private WebDriverHelper helper;
    private Tenor tenor;

    public TenorCard(WebDriverHelper webDriverHelper, Tenor tenor) {
        this.helper = webDriverHelper;
        this.tenor = tenor;
        this.tenorCardElements = new TenorCardElements(tenor);
    }

    public void openCard() {
        tenor.scrollTo();
        if (tenorCardElements.tenorCard() == null) {
            tenorCardElements.openCardButton().click();
            WebDriverHelper.quickSleep();
        }
    }

    public void closeCard() {
        tenor.scrollTo();
        if (tenorCardElements.tenorCard() != null) {
            tenorCardElements.openCardButton().click();
            WebDriverHelper.quickSleep();
        }
    }

    void markAsBechmark() {
        log.info("benchmarking tenor " + tenor.getName());
        openCard();
        try {
            tenorCardElements.benchmarkButtonActive();
            closeCard();
        } catch (NoSuchElementException e) {
            tenorCardElements.benchmarkButton().click();
        }
        closeCard();
    }

    public boolean getBrokerState(String brokerName) {
        openCard();
        try {
            String checked = tenorCardElements.brokerCheckBoxWithState(brokerName).getAttribute("checked");
            if (checked != null) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            closeCard();
        }
        return false;
    }

    public String getBrokerTimestamp(String brokerName) {
        return tenorCardElements.brokerTimeStamp(brokerName).getText();
    }

    public String getBrokerMidValue(String brokerName) {
        return tenorCardElements.brokerRates(brokerName).get(2).getText();
    }

    void switchBroker(String brokerName, boolean onInstrumentLevel, boolean onRiskGroupLevel, SwitchStates newState) {
        BrokerButton brokerCheckbox = tenorCardElements.brokerCheckBox(brokerName);
        helper.switchState(brokerCheckbox, newState, onInstrumentLevel, onRiskGroupLevel);
    }

    public List<String> getAllBrokers() {
        return tenorCardElements.allBrokers().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    void changeRate(String rate) {
        if (!tenorCardElements.rateInputField().getAttribute("value").equals(rate)) {
            helper.fillInField(tenorCardElements.rateInputField(), rate);
        }
    }

    void selectFirstWing(TenorModels tenorModel, String driverName) {
        switch (tenorModel) {
            case CODi:
                selectDependentTenor(tenorCardElements.codiFirstWingInput(), driverName);
                break;
            case FLY:
                selectDependentTenor(tenorCardElements.flyFirstWingInput(), driverName);
        }
    }

    void selectSecondWing(TenorModels tenorModel, String driverName) {
        switch (tenorModel) {
            case CODi:
                selectDependentTenor(tenorCardElements.codiSecondWingInput(), driverName);
                break;
            case FLY:
                selectDependentTenor(tenorCardElements.flySecondWingInput(), driverName);
        }
    }

    void selectDriver(String driverName) {
        if (!tenorCardElements.driverInputField().getAttribute("value").equals(driverName)) {
            selectDependentTenor(tenorCardElements.driverInputField(), driverName);
        }
    }

    private void selectDependentTenor(WebElement tenorSelectionField, String driverName) {
        if (!tenorSelectionField.getAttribute("value").equals(driverName)) {
            helper.fillInField(tenorSelectionField, driverName);
            tenorCardElements.selectDriverForTenor(driverName).click();
        }
    }

    void applyChanges(boolean apply) {
        if (apply) {
            tenorCardElements.applyButton().click();
        } else tenorCardElements.cancelButton().click();
    }

    void setFitflag(FitFlags fitflag) {
        for (WebElement e : tenorCardElements.fitFlags()) {
            if (FitFlags.valueOf(e.getText()) == fitflag) {
                e.click();
                break;
            }
        }
    }
}
