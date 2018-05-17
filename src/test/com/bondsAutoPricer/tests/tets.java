package com.bondsAutoPricer.tests;

import com.bondsAutoPricer.utils.WebDriverHelper;
import com.bondsAutoPricer.webElements.MainWindowElements;
import org.testng.annotations.Test;

import java.io.Serializable;

public class tets {

    @Test
    public void test() throws InterruptedException {
        WebDriverHelper webDriverHelper = new WebDriverHelper();
        MainWindowElements mainWindowElements = new MainWindowElements();
        webDriverHelper.start();

        webDriverHelper.getDriver().findElement(mainWindowElements.getHeaderDropDownButton()).click();
        webDriverHelper.getDriver().findElement(mainWindowElements.bondSelector("IsrMandBonds")).click();

        Thread.sleep(10000);
    }


}
