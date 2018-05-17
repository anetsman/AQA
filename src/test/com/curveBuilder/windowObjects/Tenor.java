package com.curveBuilder.windowObjects;

import com.curveBuilder.enums.FitFlags;
import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.enums.TenorModels;
import com.curveBuilder.prototypes.Button;
import com.curveBuilder.prototypes.HouseValue;
import com.curveBuilder.utils.WebDriverHelper;
import com.curveBuilder.webElements.MainWindowElements;
import com.curveBuilder.webElements.TenorElements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by anetsman on 2017-06-06.
 */
public class Tenor {

    private final WebDriverHelper helper;
    private TenorModels currentModel;
    private String name;
    private String currentHouseValue;
    private WebElement tenorElement;
    private TenorElements tenorElements;

    public Tenor(String tenorName, WebDriverHelper webDriverHelper) {
        this.helper = webDriverHelper;
        this.name = tenorName;
        this.tenorElement = new MainWindowElements().getTenorByName(name);
        this.tenorElements = new TenorElements(tenorElement);
        this.currentModel = TenorModels.valueOf(tenorElements.getHouseModel());
    }

    public void scrollTo() {
        WebElement upperTenor = new MainWindow(helper).getUpperTenor(name);
        JavascriptExecutor jse = (JavascriptExecutor) WebDriverHelper.getDriver();
        jse.executeScript("arguments[0].scrollIntoView()", upperTenor);
    }

    public TenorElements getTenorElements() {
        return tenorElements;
    }

    public WebElement getTenorElement() {
        return tenorElement;
    }

    public String getName() {
        return name;
    }

    public TenorModels getTenorModel() {
        return currentModel;
    }

    public void switchModel(TenorModels newModel, HouseValue newHouseValue) {
//        if (!getCurrentHouseValue().equals(newHouseValue.getNewValue())) {
            TenorCard tenorCard = new TenorCard(helper, this);
            tenorCard.openCard();

            if (newModel != currentModel) {
                TenorElements tenorElements = new TenorElements(tenorElement);
                tenorElements.modelDropDownButton().click();
                tenorElements.modelSelector(newModel.toString()).click();
            }

            switch (newModel) {
                case COD:
                    if (newHouseValue != null) {
                        if (newHouseValue.getNewValue() != null) {
                            tenorCard.changeRate(newHouseValue.getNewValue());
                            tenorCard.applyChanges(true);
                        }
                    } else tenorCard.applyChanges(true);
                    break;
                case SPRD:
                    tenorCard.selectDriver(newHouseValue.getDriverName());
                    tenorCard.changeRate(newHouseValue.getNewValue());
                    tenorCard.applyChanges(true);
                    break;
                case FLY:
                    tenorCard.selectFirstWing(newModel, newHouseValue.getFirstWingName());
                    tenorCard.selectSecondWing(newModel, newHouseValue.getSecondWingName());
                    tenorCard.changeRate(newHouseValue.getNewValue());
                    tenorCard.applyChanges(true);
                    break;
                case USER:
                    tenorCard.changeRate(newHouseValue.getNewValue());
                    tenorCard.applyChanges(true);
                    break;
                case CODi:
                    tenorCard.selectFirstWing(newModel, newHouseValue.getFirstWingName());
                    tenorCard.selectSecondWing(newModel, newHouseValue.getSecondWingName());
                    tenorCard.changeRate(newHouseValue.getNewValue());
                    tenorCard.applyChanges(true);
                    break;
            }
            this.currentModel = newModel;
            tenorCard.closeCard();
//        }
    }

    public void switchMarket(SwitchStates newState) {
        helper.switchState(this, tenorElements.marketButton(), newState);
    }

    public String getMarketTimestamp() {
        return tenorElements.marketButton().getTimestamp();
    }

    public String getHouseTimestamp() {
        return tenorElements.houseButton().getTimestamp();
    }

    public void switchHouse(SwitchStates newState) {
//        HashMap<WebElement, Boolean> house = tenorElements.houseButton();
        HouseButton houseButton = tenorElements.houseButton();
        if (!(houseButton.getCurrentState() == SwitchStates.PENDING)) {
            helper.switchState(this, houseButton, newState);
        }
    }

    public Button getHouseButton() {
        return tenorElements.houseButton();
    }

    public String getMarketValue() {
        currentHouseValue = (!getMarketValueImplied().equals("Implied market value not visible right now")) ? getMarketValueImplied() : getMarketValueReal();
        return currentHouseValue;
    }

    public String getMarketValueReal() {
        String marketValueReal;
        try {
            marketValueReal = tenorElements.marketValueReal().getText();
        } catch (NoSuchElementException e) {
            marketValueReal = "Real market value not visible right now";
        }
        return marketValueReal;
    }

    public String getMarketValueImplied() {
        String marketValueImplied;
        try {
            marketValueImplied = tenorElements.marketValueReal().getText();
        } catch (NoSuchElementException e) {
            marketValueImplied = "Implied market value not visible right now";
        }
        return marketValueImplied;
    }

    public String getCurveValue() {
        String curveValue = getCurveValueReal();

        if (curveValue.equals("Real curve value not visible right now")) {
            curveValue = getCurveValueImplied();
            if (curveValue.equals("Implied curve value not visible right now")) {
                curveValue = getCurveValueMismatch();
            }
        }
        return curveValue;
    }

    public String getCurveValueReal() {
        String curveValueReal;
        try {
            curveValueReal = tenorElements.curveValueReal().getText();
        } catch (NoSuchElementException e) {
            curveValueReal = "Real curve value not visible right now";
        }

        return curveValueReal;
    }

    public String getCurveValueImplied() {
        String curveValueImplied;
        try {
            curveValueImplied = tenorElements.curveValueImplied().getText();
        } catch (NoSuchElementException e) {
            curveValueImplied = "Implied curve value not visible right now";
        }

        return curveValueImplied;
    }

    public String getCurveValueMismatch() {
        String curveValueMismatch;
        try {
            curveValueMismatch = tenorElements.getCurveValueMismatch().getText();
        } catch (NoSuchElementException e) {
            curveValueMismatch = "Mismatch curve value not visible right now";
        }

        return curveValueMismatch;
    }

    public String getCurrentHouseValue() {
        currentHouseValue = (!getCurrentHouseValueReady().equals("Ready House Value is not visible right now")) ? getCurrentHouseValueReady() : getCurrentHouseValueReal();
        return currentHouseValue;
    }

    public String getCurrentHouseValueReal() {
        try {
            currentHouseValue = tenorElements.houseValueField().getAttribute("value");
        } catch (NoSuchElementException e) {
            currentHouseValue = "Real House Value is not visible right now";
        }
        return currentHouseValue;
    }

    public String getCurrentHouseValueReady() {
        String houseValueReady;
        try {
            houseValueReady = tenorElements.houseValueReadyField().getAttribute("value");
        } catch (NoSuchElementException e) {
            houseValueReady = "Ready House Value is not visible right now";
        }
        return houseValueReady;
    }

    public List<String> getMostPowerfulBroker() {
        return tenorElements.mostPowerfulBroker().stream()
                .map(WebElement::getText)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void benchmarkTenor(boolean isBenchmark) {
        if (isBenchmark) {
            if (!tenorElements.isBenchmarked()) {
                TenorCard card = new TenorCard(helper, this);
                card.markAsBechmark();
            }
        } else {
            if (tenorElements.isBenchmarked()) {
                TenorCard card = new TenorCard(helper, this);
                card.markAsBechmark();
            }
        }
    }

    public void switchBroker(String brokerName, boolean onInstrumentLevel, boolean onRiskGroupLevel, SwitchStates newState) {
        TenorCard card = new TenorCard(helper, this);
        card.openCard();
        card.switchBroker(brokerName, onInstrumentLevel, onRiskGroupLevel, newState);
        card.closeCard();
    }

    public void switchBrokers(List<String> brokerNames, SwitchStates newState) {
        TenorCard card = new TenorCard(helper, this);
        card.openCard();
        brokerNames.forEach(broker -> card.switchBroker(broker, false, false, newState));
        card.closeCard();
    }

    public List<String> getAllBrokers() {
        TenorCard card = new TenorCard(helper, this);
        card.openCard();
        return card.getAllBrokers();
    }

    public FitFlags getFitFlag() {
        return FitFlags.valueOf(tenorElements.fitFlag().getText());
    }

    public void setFitFlag(FitFlags fitFlag) {
        if (fitFlag != getFitFlag()) {
            TenorCard card = new TenorCard(helper, this);
            card.openCard();
            card.setFitflag(fitFlag);
//            PopUpInfoWindow.confirmActionOnPopUp();
            card.closeCard();
        }
    }

    public void changeRateInline(boolean ifIncrease, boolean onInstrumentLevel) {
        TenorCard card = new TenorCard(helper, this);
        this.scrollTo();
        card.closeCard();

        try {
            helper.navigateToElement(tenorElements.houseValueField());
        } catch (NoSuchElementException er) {
            helper.navigateToElement(tenorElements.houseValueReadyField());
        }

        WebElement operationButton;
        if (ifIncrease) {
            operationButton = tenorElements.addButton();
        } else {
            operationButton = tenorElements.subtractButton();
        }

        String currentValue = getCurrentHouseValue();
        if (onInstrumentLevel) {
            WebDriverHelper.performCtrlClick(operationButton);
        } else operationButton.click();

        int i = 0;
        while (i != 3) {
            if (currentValue.equals(getCurrentHouseValue())) {
                WebDriverHelper.quickSleep();
                i++;
            } else i = 3;
        }
    }

    public void setRateInline(String newRate, boolean withEnter) {
        TenorCard tenorCard = new TenorCard(helper, this);

        String currentValue = getCurrentHouseValue();

        if (!currentValue.equals(newRate)) {
            this.scrollTo();
            tenorCard.closeCard();
            WebElement rateField;
            try {
                rateField = tenorElements.houseValueField();
                helper.fillInField(rateField, newRate);
            } catch (NoSuchElementException er) {
                rateField = tenorElements.houseValueReadyField();
                helper.fillInField(rateField, newRate);
            }
            if (!withEnter) {
                tenorElements.approveButton().click();
            } else rateField.sendKeys(Keys.ENTER);

            int i = 0;
            while (i != 3) {
                if (currentValue.equals(getCurrentHouseValue())) {
                    WebDriverHelper.quickSleep();
                    i++;
                } else i = 3;
            }
        }
    }

    public void setHouseRate(HouseValue newHouseValue) {
        if (!getCurrentHouseValue().equals(newHouseValue.getNewValue())) {
            TenorCard card = new TenorCard(helper, this);
            card.openCard();
            card.changeRate(newHouseValue.getNewValue());
            card.applyChanges(true);
            card.closeCard();
        }
    }
}
