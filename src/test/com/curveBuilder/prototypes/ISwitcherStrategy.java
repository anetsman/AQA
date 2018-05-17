package com.curveBuilder.prototypes;

import com.curveBuilder.windowObjects.PopUpInfoWindow;
import com.curveBuilder.utils.WebDriverHelper;
import org.openqa.selenium.WebDriverException;

import java.util.NoSuchElementException;

public interface ISwitcherStrategy {
    void switchToState();

    default void switchState(Button button) {
        button.getButtonElement().click();
        try {
            PopUpInfoWindow.confirmActionOnPopUp();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } catch (WebDriverException er) {
        }
        WebDriverHelper.quickSleep();
    }
}
