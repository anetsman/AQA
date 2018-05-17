package com.curveBuilder.windowObjects;

import com.curveBuilder.utils.WebDriverHelper;
import com.curveBuilder.webElements.PopUpInfoElements;

import java.util.NoSuchElementException;

public class PopUpInfoWindow {

    private static final PopUpInfoElements popUpElements = new PopUpInfoElements(WebDriverHelper.getDriver());

    public PopUpInfoWindow() {
    }

    public static void confirmActionOnPopUp() {
        if (popUpElements.isPopUpDisplayed()) {
            try {
                popUpElements.popUpButton("Yes").click();
            } catch (NoSuchElementException e) {
                popUpElements.popUpButton("OK").click();
            }
        }
    }
}
