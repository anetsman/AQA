package com.curveBuilder.windowObjects;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.prototypes.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MarketButton extends Button {

    public MarketButton(WebElement button, SwitchStates currentState) {
        super(button, currentState);
    }

    @Override
    public String getTimestamp() {
        return getButtonElement().findElement(By.xpath("./time")).getText();
    }
}
