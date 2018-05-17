package com.curveBuilder.tests;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.enums.TenorModels;
import com.curveBuilder.prototypes.HouseValue;
import com.curveBuilder.utils.WebDriverHelper;
import com.curveBuilder.windowObjects.MainWindow;
import com.curveBuilder.windowObjects.Tenor;
import com.curveBuilder.windowObjects.TenorCard;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

public class MarketModelSuite {
//    final static Logger logger = Logger.getLogger(MarketModelSuite.class);
    private static WebDriverHelper webDriverHelper;
    private static final List<String> tenors = new ArrayList<String>() {{
        add("5x8 Fra");
        add("6Y Swp");
    }};

    private static Tenor tenor5x8;
    private static TenorCard tenor5x8Card;
    private static Tenor tenor6Y;
    private static TenorCard tenor6YCard;

    @BeforeClass
    public static void startBrowser() {
        webDriverHelper = new WebDriverHelper();
        webDriverHelper.start();
        MainWindow mainWindow = new MainWindow(webDriverHelper);
        mainWindow.selectCurve("CZK Trader");
        mainWindow.switchToRiskGroup("LIBOR");

        tenor5x8 = new Tenor(tenors.get(0), webDriverHelper);
        tenor6Y = new Tenor(tenors.get(1), webDriverHelper);
        tenor5x8Card = new TenorCard(webDriverHelper, tenor5x8);
        tenor6YCard = new TenorCard(webDriverHelper, tenor6Y);

        tenor5x8.switchModel(TenorModels.USER,
                new HouseValue.HouseModelBuilder()
                    .newValue("1.0000")
                    .build());
        tenor6Y.switchModel(TenorModels.USER,
                new HouseValue.HouseModelBuilder()
                        .newValue("1.0000")
                        .build());

        tenor5x8.switchBrokers(tenor5x8.getAllBrokers(), SwitchStates.OFF);
        tenor5x8.switchBroker("ICAP", false, false, SwitchStates.ON);

        tenor6Y.switchBrokers(tenor6Y.getAllBrokers(), SwitchStates.OFF);
        tenor6Y.switchBroker("FTFS", false, false, SwitchStates.ON);
    }

    @BeforeMethod
    public static void setUp() {
        tenor5x8.switchHouse(SwitchStates.OFF);
        tenor5x8.switchMarket(SwitchStates.ON);
        tenor6Y.switchHouse(SwitchStates.OFF);
        tenor6Y.switchMarket(SwitchStates.ON);
    }

    @AfterClass
    public static void closeBrowser() {
        WebDriverHelper.getDriver().close();
        WebDriverHelper.getDriver().quit();
    }

    @AfterMethod
    public static void cleanUp() {
        tenor5x8Card.closeCard();
        tenor6YCard.closeCard();
    }

    @Test(description = "Market rate equals Curve rate")
    public void curveAndMarketRatesAreEqualTest() {
        Assert.assertEquals(tenor5x8.getCurveValueReal(), tenor5x8.getMarketValueReal(),
                "Curve and Market rates are not equal for tenor " + tenor5x8.getName());
        Assert.assertEquals(tenor6Y.getCurveValueReal(), tenor6Y.getMarketValueReal(),
                "Curve and Market rates are not equal for tenor " + tenor6Y.getName());
    }

    @Test(description = "check that broker timestamp == IMPR button timestamp")
    public void timestampsAreEqualTest() {
        tenor5x8Card.openCard();
        Assert.assertEquals(tenor5x8Card.getBrokerTimestamp("ICAP"), tenor5x8.getMarketTimestamp(),
                "Broker timestamp not equals to IMPR button one for tenor " + tenor5x8.getName());


        tenor6YCard.openCard();
        Assert.assertEquals(tenor6YCard.getBrokerTimestamp("FTFS"), tenor6Y.getMarketTimestamp(),
                "Broker timestamp not equals to IMPR button one for tenor " + tenor6Y.getName());
    }

    @Test(description = "Check that column for most powerful broker is shown")
    public void columnForMostPowerfulBrokerShownTest() {
        tenor5x8Card.openCard();
        Assert.assertEquals(tenor5x8.getMostPowerfulBroker().get(0), "ICAP",
                "most powerful broker is not as expected for tenor " + tenor5x8.getName());
        Assert.assertEquals(tenor5x8.getMostPowerfulBroker().get(1), tenor5x8Card.getBrokerMidValue("ICAP"),
                "most powerful broker rate is not as expected for tenor " + tenor5x8.getName());

        tenor6YCard.openCard();
        Assert.assertEquals(tenor6Y.getMostPowerfulBroker().get(0), "FTFS",
                "most powerful broker is not as expected for tenor " + tenor6Y.getName());
        Assert.assertEquals(tenor6Y.getMostPowerfulBroker().get(1), tenor6YCard.getBrokerMidValue("FTFS"),
                "most powerful broker rate is not as expected for tenor " + tenor6Y.getName());
    }

    @Test(description = "selecting different brokers as source, asserting equals timestamps for broker and market", priority = 1)
    public void differentBrokersAsASourceFirstTest() {
        tenor5x8.switchBroker("BGCP", false, false, SwitchStates.ON);
        tenor5x8.switchBroker("ICAP", false, false, SwitchStates.OFF);

        tenor5x8Card.openCard();
        Assert.assertEquals(tenor5x8Card.getBrokerTimestamp("BGCP"), tenor5x8.getMarketTimestamp(),
                "Broker timestamp not equals to IMPR button one for tenor " + tenor5x8.getName());

        tenor6Y.switchBroker("TTKL", false, false, SwitchStates.ON);
        tenor6Y.switchBroker("FTFS", false, false, SwitchStates.OFF);

        tenor6YCard.openCard();
        Assert.assertEquals(tenor6YCard.getBrokerTimestamp("FTFS"), tenor6Y.getMarketTimestamp(),
                "Broker timestamp not equals to IMPR button one for tenor " + tenor6Y.getName());
    }

    @Test(description = "selecting different brokers as source, asserting equals values for broker and market", priority = 2)
    public void differentBrokersAsASourceSecondTest() {
        tenor5x8Card.openCard();
        Assert.assertEquals(tenor5x8.getMarketValueReal(), tenor5x8Card.getBrokerMidValue("BGCP"),
                "Broker Rate and Market Rate are not equal for tenor " + tenor5x8.getName());

        tenor6YCard.openCard();
        Assert.assertEquals(tenor6Y.getMarketValueReal(), tenor6YCard.getBrokerMidValue("TTKL"),
                "Broker Rate and Market Rate are not equal for tenor " + tenor6Y.getName());

    }

    @Test(description = "selecting different brokers as source, asserting equals Curve and Market Rates", priority = 3)
    public void differentBrokersAsASourceThirdTest() {
        Assert.assertEquals(tenor5x8.getCurveValueReal(), tenor5x8.getMarketValueReal(),
                "Curve and Market rates are not equal for tenor " + tenor5x8.getName());
        Assert.assertEquals(tenor6Y.getCurveValueReal(), tenor6Y.getMarketValueReal(),
                "Curve and Market rates are not equal for tenor " + tenor6Y.getName());
    }

    @Test(description = "enabling House model back", priority = 4)
    public void enableHouseLegTest() {
        tenor5x8.switchHouse(SwitchStates.ON);
        WebDriverHelper.longSleep();
        try {
            Assert.assertNotEquals(tenor5x8.getCurveValueReal(), tenor5x8.getMarketValueReal(),
                    "Curve and Market rates are equals for tenor " + tenor5x8.getName());
        } catch (AssertionError error) {
            WebDriverHelper.longSleep();
            Assert.assertNotEquals(tenor5x8.getCurveValueReal(), tenor5x8.getMarketValueReal(),
                    "Curve and Market rates are equals for tenor " + tenor5x8.getName());
        }

        tenor6Y.switchHouse(SwitchStates.ON);
        WebDriverHelper.longSleep();
        try {
            Assert.assertNotEquals(tenor6Y.getCurveValueReal(), tenor6Y.getMarketValueReal(),
                    "Curve and Market rates are equals for tenor " + tenor6Y.getName());
        } catch (AssertionError error) {
            WebDriverHelper.longSleep();
            Assert.assertNotEquals(tenor6Y.getCurveValueReal(), tenor6Y.getMarketValueReal(),
                    "Curve and Market rates are equals for tenor " + tenor6Y.getName());
        }
    }
}
