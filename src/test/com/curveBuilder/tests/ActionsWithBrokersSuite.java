package com.curveBuilder.tests;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.windowObjects.MainWindow;
import com.curveBuilder.windowObjects.Tenor;
import com.curveBuilder.windowObjects.TenorCard;
import com.curveBuilder.utils.WebDriverHelper;
import org.junit.Ignore;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

public class ActionsWithBrokersSuite {

    private SoftAssert softAssert = new SoftAssert();
    private static WebDriverHelper webDriverHelper;
    private static final List<String> tenors = new ArrayList<String>() {{
        add("6x9 Fra");
        add("5Y Swp");
    }};

    private static Tenor tenor6x9;
    private static TenorCard tenor6x9Card;
    private static Tenor tenor5Y;
    private static TenorCard tenor5YCard;

    @BeforeClass
    public static void startBrowser() {
        webDriverHelper = new WebDriverHelper();
        webDriverHelper.start();
        MainWindow mainWindow = new MainWindow(webDriverHelper);
        mainWindow.selectCurve("CZK (2) Trader");
        mainWindow.switchToRiskGroup("LIBOR");

        tenor6x9 = new Tenor(tenors.get(0), webDriverHelper);
        tenor6x9Card = new TenorCard(webDriverHelper, tenor6x9);
        tenor5Y = new Tenor(tenors.get(1), webDriverHelper);
        tenor5YCard = new TenorCard(webDriverHelper, tenor5Y);
    }

    @AfterClass
    public static void closeBrowser() {
        WebDriverHelper.getDriver().close();
        WebDriverHelper.getDriver().quit();
    }

    @AfterMethod
    public static void cleanUp() {
        tenor6x9Card.closeCard();
        tenor5YCard.closeCard();
    }

    @Ignore
    @Test
    public void marketRateChangedOnBrokerSwitching() {
        String brokerName = "TTKL";
        tenor5Y.switchBroker(brokerName, false, false, SwitchStates.ON);
        WebDriverHelper.quickSleep();
    }
}
