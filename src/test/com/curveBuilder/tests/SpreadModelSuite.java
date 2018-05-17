package com.curveBuilder.tests;

import com.curveBuilder.enums.FitFlags;
import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.enums.TenorModels;
import com.curveBuilder.prototypes.HouseValue;
import com.curveBuilder.utils.RandomizeTenors;
import com.curveBuilder.utils.WebDriverHelper;
import com.curveBuilder.windowObjects.MainWindow;
import com.curveBuilder.windowObjects.Tenor;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class SpreadModelSuite {
    private SoftAssert softAssert = new SoftAssert();
    private static List<String> tenors;

    private static Tenor tenor1;
    private static Tenor tenor2;
    private static Tenor tenor3;
    private static Tenor tenor4;
    private static Tenor tenor5;
    private static Tenor tenor6;

    private static Double globalStep = 0.2;

    @BeforeClass
    public static void setUp() {
        WebDriverHelper webDriverHelper = new WebDriverHelper();
        webDriverHelper.start();
        MainWindow mainWindow = new MainWindow(webDriverHelper);
        mainWindow.selectCurve("MYR Test only ND");
        mainWindow.switchToRiskGroup("LIBOR.ND");
        mainWindow.setGlobalStep(globalStep, true);

        RandomizeTenors randomizeTenors = new RandomizeTenors.RandomTenorsBuilder(mainWindow)
                        .numberOfTenors(6)
                        .fromOneInstrument(true)
                        .build();
        tenors = randomizeTenors.getRandomTenors();

        tenor1 = new Tenor(tenors.get(0), webDriverHelper);
        tenor1.switchModel(TenorModels.USER, new HouseValue.HouseModelBuilder()
                        .newValue("1.0000")
                        .build());
        tenor1.switchHouse(SwitchStates.ON);

        tenor2 = new Tenor(tenors.get(1), webDriverHelper);
        tenor2.switchModel(TenorModels.USER, new HouseValue.HouseModelBuilder()
                        .newValue("1.0000")
                        .build());
        tenor2.switchHouse(SwitchStates.ON);


        tenor3 = new Tenor(tenors.get(2), webDriverHelper);
        tenor3.switchModel(TenorModels.SPRD, new HouseValue.HouseModelBuilder()
                .driverName(tenor1.getName())
                .newValue("1.0")
                .build());
        tenor3.switchHouse(SwitchStates.ON);


        tenor4 = new Tenor(tenors.get(3), webDriverHelper);
        tenor4.switchModel(TenorModels.SPRD, new HouseValue.HouseModelBuilder()
                .driverName(tenor2.getName())
                .newValue("1.000")
                .build());
        tenor4.switchHouse(SwitchStates.ON);


        tenor5 = new Tenor(tenors.get(4), webDriverHelper);
        tenor5.switchModel(TenorModels.SPRD, new HouseValue.HouseModelBuilder()
                .driverName(tenor3.getName())
                .newValue("1.00")
                .build());
        tenor5.switchHouse(SwitchStates.ON);


        tenor6 = new Tenor(tenors.get(5), webDriverHelper);
        tenor6.switchModel(TenorModels.SPRD, new HouseValue.HouseModelBuilder()
                .driverName(tenor5.getName())
                .newValue("1")
                .build());
        tenor6.switchHouse(SwitchStates.ON);
    }

    @AfterClass
    public static void closeBrowser() {
        WebDriverHelper.getDriver().close();
        WebDriverHelper.getDriver().quit();
    }

    @Test(description = "changing driver rate affects changing all dependent rates")
    public void changeDriverRateTest() {
        String tenor8YCurveRate = tenor3.getCurveValue();
        String tenor10YCurveRate = tenor5.getCurveValue();
        String tenor12YCurveRate = tenor6.getCurveValue();

        tenor1.setRateInline("0.1", false);

        softAssert.assertNotEquals(tenor3.getCurveValue(), tenor8YCurveRate,
                "Curve Rate for 8Y tenor not changed");
        softAssert.assertNotEquals(tenor5.getCurveValue(), tenor10YCurveRate,
                "Curve Rate for 10Y tenor not changed");
        softAssert.assertNotEquals(tenor6.getCurveValue(), tenor12YCurveRate,
                "Curve Rate for 12Y tenor not changed");

        Assert.assertEquals(tenor1.getHouseTimestamp(), tenor6.getHouseTimestamp(),
                "House Timestamps for driver and driven tenors not the same");
    }

    @Test(description = "increasing spread driver rate by step value and checking all dependencies")
    public void increaseDriverRateByStepTest() {
        Double tenor8YHouseRate = Double.valueOf(tenor3.getCurrentHouseValue());

        String tenor8YCurveRate = tenor3.getCurveValue();
        String tenor10YCurveRate = tenor5.getCurveValue();
        String tenor12YCurveRate = tenor6.getCurveValue();

        tenor3.changeRateInline(true, false);

        softAssert.assertNotEquals(tenor3.getCurveValue(), tenor8YCurveRate,
                "Curve Rate for 8Y tenor not changed");
        softAssert.assertNotEquals(tenor5.getCurveValue(), tenor10YCurveRate,
                "Curve Rate for 10Y tenor not changed");
        softAssert.assertNotEquals(tenor6.getCurveValue(), tenor12YCurveRate,
                "Curve Rate for 12Y tenor not changed");

        Assert.assertEquals(tenor3.getHouseTimestamp(), tenor6.getHouseTimestamp(),
                "House Timestamps for driver and driven tenors not the same");
        Assert.assertEquals(Double.valueOf(tenor3.getCurrentHouseValue()),
                Math.round((tenor8YHouseRate + globalStep) * 100.0) / 100.0,
                "Rate not changed for 8Y Swap tenor");
    }

    @Test(dataProvider = "values", description = "Change House rate for spread tenor inline with hitting Enter")
    public void changeRateInlineWithEnterTest(String value) {
        String tenor12YTimestamp = tenor6.getHouseTimestamp();
        tenor6.setRateInline(value, true);
        WebDriverHelper.quickSleep();

        Assert.assertNotEquals(tenor6.getHouseTimestamp(), tenor12YTimestamp,
                "House Timestamp for tenor 12Y not changed");
        Assert.assertEquals(Double.valueOf(tenor6.getCurrentHouseValue()), Double.valueOf(value),
                "Rate not changed for 12Y Swap tenor");
    }

    @Test(dataProvider = "values", description = "Change House rate for spread tenor inline with clicking Approve button")
    public void changeRateInlineWithApproveBtnTest(String value) {
        String tenor10YTimestamp = tenor5.getHouseTimestamp();
        tenor5.setRateInline(value, false);
        WebDriverHelper.quickSleep();

        Assert.assertNotEquals(tenor5.getHouseTimestamp(), tenor10YTimestamp,
                "House Timestamp for tenor 10Y not changed");
        Assert.assertEquals(Double.valueOf(tenor5.getCurrentHouseValue()), Double.valueOf(value),
                "Rate not changed for 10Y Swap tenor");
    }

    @Test(description = "disabling both legs for SPRD model tenor results in setting dependent tenors House off", priority = 1)
    public void disablingBothLegsTest() {
        tenor3.switchMarket(SwitchStates.OFF);
        tenor3.switchHouse(SwitchStates.OFF);
        WebDriverHelper.quickSleep();

        FitFlags fitFlag8Y = tenor3.getFitFlag();
        softAssert.assertTrue(fitFlag8Y.equals(FitFlags.S) || fitFlag8Y.equals(FitFlags.I),
                "fit flag not changed and stay " + fitFlag8Y.getFlag());
        softAssert.assertEquals(tenor4.getHouseButton().getCurrentState(), SwitchStates.ON,
                "House leg state for not dependent tenor is changed");
        Assert.assertEquals(tenor5.getHouseButton().getCurrentState(), SwitchStates.OFF,
                "State for House for 10Y Swp is not OFF");
        Assert.assertEquals(tenor6.getHouseButton().getCurrentState(), SwitchStates.OFF,
                "State for House for 12Y Swp is not OFF");
    }

    @Test(description = "enabling both legs back, dependent tenors stay off", priority = 2)
    public void enableBothLegsTest() {
        String curveValue8Y = tenor3.getCurveValue();
        tenor3.switchHouse(SwitchStates.ON);
        tenor3.switchMarket(SwitchStates.ON);
        WebDriverHelper.quickSleep();

        FitFlags fitFlag8Y = tenor3.getFitFlag();
        softAssert.assertTrue(fitFlag8Y.equals(FitFlags.F) || fitFlag8Y.equals(FitFlags.O),
                "fit flag not changed and stay " + fitFlag8Y.getFlag());
        softAssert.assertEquals(tenor4.getHouseButton().getCurrentState(), SwitchStates.ON,
                "House leg state for not dependent tenor is changed");

        Assert.assertNotEquals(tenor3.getCurveValue(), curveValue8Y, "Curve value not changed!");
        Assert.assertEquals(tenor5.getHouseButton().getCurrentState(), SwitchStates.OFF
                , "State for House for 10Y Swp is not OFF");
        Assert.assertEquals(tenor6.getHouseButton().getCurrentState(), SwitchStates.OFF,
                "State for House for 12Y Swp is not OFF");
    }

    @Test(description = "setting House for one SPRD tenor to Ready setting dependent tenor's House to Ready", priority = 3)
    public void settingHouseReadyTest() {
        tenor6.switchHouse(SwitchStates.READY);

        softAssert.assertEquals(tenor4.getHouseButton().getCurrentState(), SwitchStates.ON,
                "House leg state for not dependent tenor is changed");

        Assert.assertEquals(tenor6.getHouseButton().getCurrentState(), SwitchStates.READY,
                "house leg for 12Y tenor is not READY");
        Assert.assertEquals(tenor5.getHouseButton().getCurrentState(), SwitchStates.READY,
                "house leg for 10Y tenor is not READY");
    }

    @Test(description = "enabling House for one SPRD tenor not affecting dependent tenor's House in Ready state", priority = 4)
    public void enablingHouseTest() {
        tenor6.switchHouse(SwitchStates.ON);

        softAssert.assertEquals(tenor4.getHouseButton().getCurrentState(), SwitchStates.ON,
                "House leg state for not dependent tenor is changed");

        Assert.assertEquals(tenor6.getHouseButton().getCurrentState(), SwitchStates.ON,
                "house leg for 12Y tenor is not ON");
        Assert.assertEquals(tenor5.getHouseButton().getCurrentState(), SwitchStates.READY,
                "house leg for 10Y tenor is not READY");
    }

    @DataProvider(name = "values")
    public Object[][] valuesProvider() {
        return new Object[][]{
                {"0.00"}, {"-0.1"}, {"0.1"}
        };
    }
}
