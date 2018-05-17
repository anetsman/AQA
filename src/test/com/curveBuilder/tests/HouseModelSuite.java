package com.curveBuilder.tests;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.windowObjects.MainWindow;
import com.curveBuilder.windowObjects.TenorCard;
import com.curveBuilder.enums.TenorModels;
import com.curveBuilder.prototypes.HouseValue;
import com.curveBuilder.windowObjects.Tenor;
import com.curveBuilder.utils.WebDriverHelper;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

public class HouseModelSuite {
    private static final List<String> tenors = new ArrayList<String>() {{
        add("3M Xbas");
        add("1Y FxP$");
    }};

    private static Tenor tenor3MXbas;
    private static TenorCard tenor3MXbasCard;
    private static Tenor tenor1YFxP;
    private static TenorCard tenor1YFxPCard;

    @BeforeClass
    public static void startBrowser() {
        WebDriverHelper webDriverHelper = new WebDriverHelper();
        webDriverHelper.start();
        MainWindow mainWindow = new MainWindow(webDriverHelper);
        mainWindow.selectCurve("SGD Trader");
        mainWindow.switchToRiskGroup("USD.XCCY");

        tenor3MXbas = new Tenor(tenors.get(0), webDriverHelper);
        tenor3MXbasCard = new TenorCard(webDriverHelper, tenor3MXbas);
        tenor1YFxP = new Tenor(tenors.get(1), webDriverHelper);
        tenor1YFxPCard = new TenorCard(webDriverHelper, tenor1YFxP);

        tenor3MXbas.switchModel(TenorModels.USER,
                new HouseValue.HouseModelBuilder()
                        .newValue("1.00")
                        .build());

        tenor3MXbas.switchBroker("BGCP", false, false, SwitchStates.OFF);

        tenor1YFxP.switchModel(TenorModels.USER,
                new HouseValue.HouseModelBuilder()
                        .newValue("1.00")
                        .build());

        tenor3MXbas.switchHouse(SwitchStates.ON);
        tenor3MXbas.switchMarket(SwitchStates.OFF);
        tenor1YFxP.switchHouse(SwitchStates.ON);
        tenor1YFxP.switchMarket(SwitchStates.OFF);
    }

    @BeforeMethod
    public static void setUp() {

    }

    @AfterClass
    public static void closeBrowser() {
        tenor3MXbasCard.closeCard();
        tenor1YFxPCard.closeCard();
        WebDriverHelper.getDriver().close();
        WebDriverHelper.getDriver().quit();
    }

    @AfterMethod
    public static void cleanUp() {

    }

    @Test(description = "Curve rate == House rate")
    public void curveRateEqualsToHouseTest() {
        Assert.assertEquals(tenor3MXbas.getCurrentHouseValueReal(), tenor3MXbas.getCurveValueReal(),
                "Curve rate not equals to House one for tenor " + tenor3MXbas.getName());

        Assert.assertEquals(tenor1YFxP.getCurrentHouseValueReal(), tenor1YFxP.getCurveValueReal(),
                "Curve rate not equals to House one for tenor " + tenor1YFxP.getName());
    }

    @Test(description = "Column for most powerful broker should not be shown")
    public void columnForPowerfulBrokerNotShownTest() {
        Assert.assertTrue(tenor3MXbas.getMostPowerfulBroker().isEmpty(),
                "Most Powerful broker is shown for tenor " + tenor3MXbas.getName());

        Assert.assertTrue(tenor1YFxP.getMostPowerfulBroker().isEmpty(),
                "Most Powerful broker is shown for tenor " + tenor1YFxP.getName());
    }

    @Test(dataProvider = "values", description = "Set different values for House leg")
    public void setHouseToDifferentValuesTest(String value) {
        tenor1YFxP.setHouseRate(new HouseValue.HouseModelBuilder()
                .newValue(value)
                .build());
        WebDriverHelper.longSleep();
        try {
            Assert.assertEquals(tenor1YFxP.getCurveValueReal(), value,
                    "Curve rate not equals to House one for tenor " + tenor1YFxP.getName());
        } catch (AssertionError error) {
            WebDriverHelper.longSleep();
            Assert.assertEquals(tenor1YFxP.getCurveValueReal(), value,
                    "Curve rate not equals to House one for tenor " + tenor1YFxP.getName());
        }
    }

    @Test(dataProvider = "values", description = "Set different values inline for House leg")
    public void setHouseToDifferentValuesInlineTest(String value) {
        tenor3MXbas.setRateInline(value, false);
        WebDriverHelper.longSleep();
        try {
            Assert.assertEquals(tenor3MXbas.getCurveValueReal(), value,
                    "Curve rate not equals to House one for tenor " + tenor3MXbas.getName());
        } catch (AssertionError error) {
            int i = 0;
            while (i != 3) {
                if (!value.equals(tenor1YFxP.getCurveValueReal())) {
                    WebDriverHelper.longSleep();
                    i++;
                } else i = 3;
            }
            Assert.assertEquals(tenor3MXbas.getCurveValueReal(), value,
                    "Curve rate not equals to House one for tenor " + tenor3MXbas.getName());
        }
    }

    @Test(dataProvider = "limitValues", description = "Set limit values for House leg")
    public void setHouseLimitValuesTest(String value) {
        tenor3MXbas.setHouseRate(new HouseValue.HouseModelBuilder()
                .newValue(value)
                .build());
        WebDriverHelper.longSleep();
        try {
            Assert.assertEquals(tenor3MXbas.getCurveValueReal(), value,
                    "Curve rate not equals to House one for tenor " + tenor3MXbas.getName());
        } catch (AssertionError error) {
            WebDriverHelper.longSleep();
            Assert.assertEquals(tenor3MXbas.getCurveValueReal(), value,
                    "Curve rate not equals to House one for tenor " + tenor3MXbas.getName());
        }
    }

    @Test(dataProvider = "limitValues", description = "Set limit values inline with Enter for House leg")
    public void setHouseLimitValuesInlineTest(String value) {
        tenor1YFxP.setRateInline(value, true);
        WebDriverHelper.longSleep();
        WebDriverHelper.longSleep();
        try {
            Assert.assertEquals(tenor1YFxP.getCurveValueReal(), value,
                    "Curve rate not equals to House one for tenor " + tenor1YFxP.getName());
        } catch (AssertionError error) {
            int i = 0;
            while (i != 3) {
                if (!value.equals(tenor1YFxP.getCurveValueReal())) {
                    WebDriverHelper.longSleep();
                    i++;
                } else i = 3;
            }
            Assert.assertEquals(tenor1YFxP.getCurveValueReal(), value,
                    "Curve rate not equals to House one for tenor " + tenor1YFxP.getName());
        }
    }

    @Test(description = "enabling market back", priority = 1)
    public void enableMarketBackTest() {
        tenor3MXbas.switchBroker("BGCP", false, false, SwitchStates.ON);
        tenor3MXbas.switchMarket(SwitchStates.ON);
        WebDriverHelper.longSleep();
        Assert.assertNotEquals(tenor3MXbas.getCurveValueReal(), tenor3MXbas.getCurrentHouseValue(),
                "Curve rate equals to House for tenor " + tenor3MXbas.getName());

        tenor1YFxP.switchMarket(SwitchStates.ON);
        WebDriverHelper.longSleep();
        Assert.assertNotEquals(tenor3MXbas.getCurveValueReal(), tenor1YFxP.getCurrentHouseValue(),
                "Curve rate equals to House for tenor " + tenor1YFxP.getName());
    }

    @DataProvider(name = "values")
    public Object[][] valuesProvider() {
        return new Object[][]{
                {"0.00"}, {"1.00"}, {"-1.00"}
        };
    }

    @DataProvider(name = "limitValues")
    public Object[][] limitValuesProvider() {
        return new Object[][]{
                {"0.01"}, {"-0.01"}
        };
    }
}
