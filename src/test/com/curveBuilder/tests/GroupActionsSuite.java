package com.curveBuilder.tests;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.enums.TenorModels;
import com.curveBuilder.prototypes.HouseValue;
import com.curveBuilder.windowObjects.MainWindow;
import com.curveBuilder.windowObjects.Tenor;
import com.curveBuilder.windowObjects.TenorCard;
import com.curveBuilder.utils.WebDriverHelper;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

public class GroupActionsSuite {
    private SoftAssert softAssert = new SoftAssert();
    private static final List<String> tenors = new ArrayList<String>() {{
        add("9x12 Fra");
        add("12x15 Fra");
        add("21x24 Fra");
        add("1Y Swp");
        add("2Y Swp");
        add("3Y Swp");
    }};

    private static Tenor tenor9x12;
    private static TenorCard tenor9x12Card;
    private static Tenor tenor12x15;
    private static Tenor tenor21x24;
    private static TenorCard tenor21x24Card;
    private static Tenor tenor1Y;
    private static Tenor tenor2Y;
    private static TenorCard tenor2YCard;
    private static Tenor tenor3Y;
    private static TenorCard tenor3YCard;

    @BeforeClass
    public static void startBrowser() {
        WebDriverHelper webDriverHelper = new WebDriverHelper();
        webDriverHelper.start();
        MainWindow mainWindow = new MainWindow(webDriverHelper);
        mainWindow.selectCurve("PLN (1) Test only");
        mainWindow.switchToRiskGroup("LIBOR");

        mainWindow.setGlobalStep(0.2, true);

        tenor9x12 = new Tenor(tenors.get(0), webDriverHelper);
        tenor9x12Card = new TenorCard(webDriverHelper, tenor9x12);

        tenor12x15 = new Tenor(tenors.get(1), webDriverHelper);

        tenor21x24 = new Tenor(tenors.get(2), webDriverHelper);
        tenor21x24Card = new TenorCard(webDriverHelper, tenor21x24);

        tenor1Y = new Tenor(tenors.get(3), webDriverHelper);

        tenor2Y = new Tenor(tenors.get(4), webDriverHelper);
        tenor2YCard = new TenorCard(webDriverHelper, tenor2Y);

        tenor3Y = new Tenor(tenors.get(5), webDriverHelper);
        tenor3YCard = new TenorCard(webDriverHelper, tenor3Y);
    }

    @BeforeGroups("change-rate-inline")
    public static void setUp() {
        tenor9x12.switchModel(TenorModels.USER,
                new HouseValue.HouseModelBuilder()
                        .newValue("1.0000")
                        .build());
        tenor12x15.switchModel(TenorModels.USER,
                new HouseValue.HouseModelBuilder()
                        .newValue("1.0000")
                        .build());
        tenor1Y.switchModel(TenorModels.USER,
                new HouseValue.HouseModelBuilder()
                        .newValue("1.0000")
                        .build());
        tenor2Y.switchModel(TenorModels.USER,
                new HouseValue.HouseModelBuilder()
                        .newValue("1.0000")
                        .build());
    }

    @AfterClass
    public static void closeBrowser() {
        WebDriverHelper.getDriver().close();
        WebDriverHelper.getDriver().quit();
    }

//    @AfterMethod
//    public static void cleanUp() {
//        tenor9x12Card.closeCard();
//        tenor21x24Card.closeCard();
//        tenor2YCard.closeCard();
//        tenor3YCard.closeCard();
//    }

    @Test(description = "Disables broker for one tenor")
    public void disableBrokerForOneTenorTest() {
        String brokerName = "CCMK";
        tenor2Y.switchBroker(brokerName, false, false, SwitchStates.OFF);
        WebDriverHelper.quickSleep();

        softAssert.assertFalse(tenor2YCard.getBrokerState(brokerName),
                String.format("broker %s is enabled for tenor %s", brokerName, tenor2Y.getName()));
    }

    @Test(description = "Enables broker on Risk group level")
    public void enableBrokerOnRikGroupLevelTest() {
        String brokerName = "CCMK";
        tenor9x12.switchBroker(brokerName, false, true, SwitchStates.OFF);
        WebDriverHelper.quickSleep();

        softAssert.assertTrue(tenor9x12Card.getBrokerState(brokerName),
                String.format("broker %s is disabled for tenor %s", brokerName, tenor9x12.getName()));

        softAssert.assertTrue(tenor21x24Card.getBrokerState(brokerName),
                String.format("broker %s is disabled for tenor %s", brokerName, tenor21x24.getName()));

        softAssert.assertTrue(tenor2YCard.getBrokerState(brokerName),
                String.format("broker %s is disabled for tenor %s", brokerName, tenor2Y.getName()));

        softAssert.assertTrue(tenor3YCard.getBrokerState(brokerName),
                String.format("broker %s is disabled for tenor %s", brokerName, tenor3Y.getName()));

    }

    @Test(description = "Disables broker on Risk group level")
    public void disableBrokerOnRiskGroupLevelTest() {
        String brokerName = "ICPL";
        tenor21x24.switchBroker(brokerName, false, true, SwitchStates.ON);
        WebDriverHelper.quickSleep();

        softAssert.assertTrue(tenor21x24Card.getBrokerState(brokerName),
                String.format("broker %s is enabled for tenor %s", brokerName, tenor21x24.getName()));

        softAssert.assertTrue(tenor9x12Card.getBrokerState(brokerName),
                String.format("broker %s is enabled for tenor %s", brokerName, tenor9x12.getName()));

        softAssert.assertTrue(tenor2YCard.getBrokerState(brokerName),
                String.format("broker %s is enabled for tenor %s", brokerName, tenor2Y.getName()));

        softAssert.assertTrue(tenor3YCard.getBrokerState(brokerName),
                String.format("broker %s is enabled for tenor %s", brokerName, tenor3Y.getName()));
    }

    @Test(description = "Enables broker on instrument level")
    public void enableBrokerOnInstrumentLevelTest() {
        String brokerName = "ICPL";
        tenor9x12.switchBroker(brokerName, false, false, SwitchStates.OFF);
        tenor2Y.switchBroker(brokerName, true, false, SwitchStates.ON);
        WebDriverHelper.quickSleep();

        softAssert.assertTrue(tenor2YCard.getBrokerState(brokerName),
                String.format("broker %s is disabled for tenor %s", brokerName, tenor2Y.getName()));

        softAssert.assertTrue(tenor3YCard.getBrokerState(brokerName),
                String.format("broker %s is disabled for tenor %s", brokerName, tenor3Y.getName()));
        
        softAssert.assertFalse(tenor9x12Card.getBrokerState(brokerName),
                String.format("broker %s is enabled for tenor %s", brokerName, tenor9x12.getName()));
    }

    @Test(description = "Disables broker on instrument level")
    public void disableBrokerOnInstrumentLevelTest() {
        String brokerName = "ICPL";
        tenor9x12.switchBroker(brokerName, false, false, SwitchStates.ON);
        tenor3Y.switchBroker(brokerName, true, false, SwitchStates.OFF);
        WebDriverHelper.quickSleep();

        softAssert.assertFalse(tenor2YCard.getBrokerState(brokerName),
                String.format("broker %s is enabled for tenor %s", brokerName, tenor2Y.getName()));

        softAssert.assertFalse(tenor3YCard.getBrokerState(brokerName),
                String.format("broker %s is enabled for tenor %s", brokerName, tenor3Y.getName()));

        softAssert.assertTrue(tenor9x12Card.getBrokerState(brokerName),
                String.format("broker %s is disabled for tenor %s", brokerName, tenor9x12.getName()));
    }

    @Test(description = "Increase Rate inline for Tenors with User model on instrument level",
          groups = "change-rate-inline")
    public void increaseUserModelOnStepValueTest() {
        tenor1Y.setRateInline("1.0000", false);
        tenor2Y.setRateInline("1.0000", true);
        tenor9x12.setRateInline("1.0000", false);
        tenor12x15.setRateInline("1.0000", true);

        tenor2Y.changeRateInline(true, true);
        WebDriverHelper.quickSleep();

        Assert.assertEquals(tenor1Y.getCurrentHouseValue(), "1.0020", "Rate is wrong for " + tenor1Y.getName());
        Assert.assertEquals(tenor2Y.getCurrentHouseValue(), "1.0020", "Rate is wrong for " + tenor2Y.getName());
        Assert.assertEquals(tenor9x12.getCurrentHouseValue(), "1.0000", "Rate is wrong for " + tenor9x12.getName());
        Assert.assertEquals(tenor12x15.getCurrentHouseValue(), "1.0000", "Rate is wrong for " + tenor12x15.getName());
    }

    @Test(description = "Decrease Rate inline for Tenors with User model on instrument level",
          groups = "change-rate-inline")
    public void decreaseUserModelOnStepValueTest() {
        tenor1Y.setRateInline("1.0000", true);
        tenor2Y.setRateInline("1.0000", false);
        tenor9x12.setRateInline("1.0000", true);
        tenor12x15.setRateInline("1.0000", false);

        tenor9x12.changeRateInline(false, true);
        WebDriverHelper.quickSleep();

        Assert.assertEquals(tenor1Y.getCurrentHouseValue(), "1.0000", "Rate is wrong for " + tenor1Y.getName());
        Assert.assertEquals(tenor2Y.getCurrentHouseValue(), "1.0000", "Rate is wrong for " + tenor2Y.getName());
        Assert.assertEquals(tenor9x12.getCurrentHouseValue(), "0.9980", "Rate is wrong for " + tenor9x12.getName());
        Assert.assertEquals(tenor12x15.getCurrentHouseValue(), "0.9980", "Rate is wrong for " + tenor12x15.getName());
    }
}
