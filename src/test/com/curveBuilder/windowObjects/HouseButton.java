package com.curveBuilder.windowObjects;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.prototypes.Button;
import org.openqa.selenium.WebElement;

public class HouseButton extends Button {

    public HouseButton(WebElement buttonElement, SwitchStates currentState) {
        super(buttonElement, currentState);
    }
}
