package com.curveBuilder.prototypes;

import com.curveBuilder.enums.SwitchStates;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class Button {
    private WebElement buttonElement;
    private SwitchStates currentState;
    private String currentTimestamp;

    public Button(WebElement buttonElement, SwitchStates currentState) {
        this.buttonElement = buttonElement;
        this.currentState = currentState;
    }

    public WebElement getButtonElement() {
        return buttonElement;
    }

    public SwitchStates getCurrentState() {
        return currentState;
    }

    public String getTimestamp() {
        currentTimestamp = buttonElement.findElement(By.xpath("./time")).getText();
        return currentTimestamp;
    }
}
