package com.curveBuilder.windowObjects;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.prototypes.Button;
import org.openqa.selenium.WebElement;

public class BenchmarkButton extends Button {

    public BenchmarkButton(WebElement buttonElement, SwitchStates currentState) {
        super(buttonElement, currentState);
    }
}
