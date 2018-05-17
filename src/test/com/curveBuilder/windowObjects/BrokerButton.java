package com.curveBuilder.windowObjects;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.prototypes.Button;
import org.openqa.selenium.WebElement;

public class BrokerButton extends Button {

    public BrokerButton(WebElement button, SwitchStates currentState) {
        super(button, currentState);
    }
}
