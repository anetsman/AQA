package com.curveBuilder.utils;

import org.openqa.selenium.support.PageFactory;
/**
 * Created by anetsman on 2017-06-06.
 */

public class ObjectFactory {

    public ObjectFactory(WebDriverHelper webDriverHelper){
        PageFactory.initElements(webDriverHelper.getDriver(), this);
    }
}
