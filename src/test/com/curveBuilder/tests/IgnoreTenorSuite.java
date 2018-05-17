package com.curveBuilder.tests;

import com.curveBuilder.enums.FitFlags;
import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.enums.TenorModels;
import com.curveBuilder.prototypes.HouseValue;
import com.curveBuilder.utils.RandomizeTenors;
import com.curveBuilder.utils.WebDriverHelper;
import com.curveBuilder.windowObjects.MainWindow;
import com.curveBuilder.windowObjects.Tenor;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class IgnoreTenorSuite {

    final static Logger logger = Logger.getLogger(IgnoreTenorSuite.class);
    private static  List<String> tenors;
    private static WebDriverHelper webDriverHelper;

    private static Tenor tenor1;
    private static Tenor tenor2;
    private static Tenor tenor3;
    private static Tenor tenor4;
    private static Tenor tenor5;
    private static Tenor tenor6;
    private static Tenor tenor7;
    private static Tenor tenor8;

    @BeforeClass
    public static void startBrowser() {
        webDriverHelper = new WebDriverHelper();
        webDriverHelper.start();
        MainWindow mainWindow = new MainWindow(webDriverHelper);
        mainWindow.selectCurve("PLN Trader");
        mainWindow.switchToRiskGroup("LIBOR");

        RandomizeTenors randomizeTenors = new RandomizeTenors.RandomTenorsBuilder(mainWindow)
                .numberOfTenors(8)
                .fromOneInstrument(false)
                .build();
        tenors = randomizeTenors.getRandomTenors();

        logger.info("Tenors list:\n");
        tenors.forEach(tenorName -> logger.info(String.format("tenor%s = %s", tenors.indexOf(tenorName) + 1, tenorName)));

        tenor1 = new Tenor(tenors.get(0), webDriverHelper);
        tenor1.switchModel(TenorModels.USER, new HouseValue.HouseModelBuilder()
                .newValue("1.0000")
                .build());
        tenor1.switchHouse(SwitchStates.ON);
        tenor1.switchMarket(SwitchStates.ON);

        tenor2 = new Tenor(tenors.get(1), webDriverHelper);
        tenor2.switchModel(TenorModels.USER, new HouseValue.HouseModelBuilder()
                .newValue("1.0000")
                .build());
        tenor2.switchHouse(SwitchStates.ON);
        tenor2.switchMarket(SwitchStates.ON);

        tenor3 = new Tenor(tenors.get(2), webDriverHelper);
        tenor3.switchModel(TenorModels.COD, null);
        tenor3.switchHouse(SwitchStates.ON);
        tenor3.switchMarket(SwitchStates.ON);

        tenor4 = new Tenor(tenors.get(3), webDriverHelper);
        tenor4.switchModel(TenorModels.SPRD, new HouseValue.HouseModelBuilder()
                .driverName(tenor1.getName())
                .newValue("1.0000")
                .build());
        tenor4.switchHouse(SwitchStates.ON);
        tenor4.switchMarket(SwitchStates.ON);

        tenor5 = new Tenor(tenors.get(4), webDriverHelper);
        tenor5.switchModel(TenorModels.USER, new HouseValue.HouseModelBuilder()
                .newValue("1.0000")
                .build());
        tenor5.switchHouse(SwitchStates.ON);
        tenor5.switchMarket(SwitchStates.ON);

        tenor8 = new Tenor(tenors.get(7), webDriverHelper);
        tenor8.switchModel(TenorModels.USER, new HouseValue.HouseModelBuilder()
                .newValue("1.0000")
                .build());
        tenor8.switchHouse(SwitchStates.ON);
        tenor8.switchMarket(SwitchStates.ON);

        tenor7 = new Tenor(tenors.get(6), webDriverHelper);
        tenor7.switchModel(TenorModels.CODi, new HouseValue.HouseModelBuilder()
                .firstWingName(tenor5.getName())
                .secondWingName(tenor8.getName())
                .newValue("1.0000")
                .build());
        tenor7.switchHouse(SwitchStates.ON);
        tenor7.switchMarket(SwitchStates.ON);

        tenor6 = new Tenor(tenors.get(5), webDriverHelper);
        tenor6.switchModel(TenorModels.FLY, new HouseValue.HouseModelBuilder()
                .firstWingName(tenor5.getName())
                .secondWingName(tenor8.getName())
                .newValue("1.0000")
                .build());
        tenor6.switchHouse(SwitchStates.ON);
        tenor6.switchMarket(SwitchStates.ON);
    }

    @AfterClass
    public static void closeBrowser() {
        WebDriverHelper.getDriver().close();
        WebDriverHelper.getDriver().quit();
    }

    @Test(description = "Checks that Real Market and House Rates not shown while both models are off")
    public void checkMarketAndHouseValuesTest() {
        tenor2.switchHouse(SwitchStates.ON);
        tenor2.switchMarket(SwitchStates.ON);

        tenor2.switchHouse(SwitchStates.OFF);
        tenor2.switchMarket(SwitchStates.OFF);

        Assert.assertEquals(tenor2.getMarketValueReal(), "Real market value not visible right now",
                "Real Market Value presented, while shouldn't be for tenor " + tenor2.getName());
        Assert.assertEquals(tenor2.getCurveValueReal(), "Real curve value not visible right now",
                "Real Curve Value presented, while shouldn't be for tenor " + tenor2.getName());
    }

    @Test(description = "Checks that FitFlags are S or I while both models are off")
    public void checkFitFlagsTest() {
        tenor2.switchHouse(SwitchStates.ON);
        tenor2.switchMarket(SwitchStates.ON);

        tenor2.switchHouse(SwitchStates.OFF);
        tenor2.switchMarket(SwitchStates.OFF);

        try {
            Assert.assertEquals(tenor2.getFitFlag(), FitFlags.I, "Fit Flag is not I");
        } catch (AssertionError error) {
            Assert.assertEquals(tenor2.getFitFlag(), FitFlags.S, "Fit Flag is not S or I");
        }
    }

    @Test(description = "Checks that NO Real Rates are shown for I FitFlag")
    public void ignoreTenorWithFitFlagI() {
        tenor5.setFitFlag(FitFlags.I);

        Assert.assertEquals(tenor5.getCurveValueReal(), "Real curve value not visible right now",
                "Real Curve Rate is presented, while shouldn't be for tenor " + tenor5.getName());
        Assert.assertEquals(tenor5.getMarketValueReal(), "Real market value not visible right now",
                "Market Real Rate is presented, while shouldn't be for tenor " + tenor5.getName());
        Assert.assertEquals(tenor5.getCurrentHouseValueReal(), "Real House Value is not visible right now",
                "House Real Rate is presented, while shouldn't be for tenor " + tenor5.getName());
    }

    @Test(description = "Checks that NO Real Rates are shown for S FitFlag")
    public void ignoreTenorWithFitFlagS() {
        tenor6.setFitFlag(FitFlags.S);

        Assert.assertEquals(tenor6.getCurveValueReal(), "Real curve value not visible right now",
                "Real Curve Rate is presented, while shouldn't be for tenor " + tenor6.getName());
        Assert.assertEquals(tenor6.getMarketValueReal(), "Real market value not visible right now",
                "Market Real Rate is presented, while shouldn't be for tenor " + tenor6.getName());
        Assert.assertEquals(tenor6.getCurrentHouseValueReal(), "Real House Value is not visible right now",
                "House Real Rate is presented, while shouldn't be for tenor " + tenor6.getName());
    }

    @Test(description = "Nothing changed for Ignored USER model tenor, after changing it's rate")
    public void changeDependenciesForIgnoredUserTenorTest() {
        tenor1.setFitFlag(FitFlags.I);
        tenor1.setRateInline("0.1", false);

        Assert.assertEquals(tenor1.getCurrentHouseValueReady(), "0.1000", "House value is not changed or is not Ready");
    }

    @Test(description = "Nothing changed for Ignored COD model tenor, after changing it's rate")
    public void changeDependenciesForIgnoredCodTenorTest() {
        tenor3.setFitFlag(FitFlags.S);
        tenor3.setRateInline("-0.1", true);

        Assert.assertEquals(tenor3.getCurrentHouseValueReady(), "-0.10", "House value is not changed or is not Ready");
    }

    @Test(description = "Nothing changed for Ignored CODi model tenor, after changing it's rate")
    public void changeDependenciesForIgnoredCodiTenorTest() {
        tenor7.setFitFlag(FitFlags.S);
        tenor7.setRateInline("0", false);

        Assert.assertEquals(tenor7.getCurrentHouseValueReady(), "0.00",
                "House value is not changed or is not Ready");
    }

    @Test(description = "Nothing changed for Ignored FLY model tenor, after changing it's rate")
    public void changeDependenciesForIgnoredFlyTenorTest() {
        tenor8.setFitFlag(FitFlags.I);
        tenor8.setRateInline("1.1", true);
        WebDriverHelper.quickSleep();

        Assert.assertEquals(tenor8.getCurrentHouseValueReady(), "1.1000",
                "House value is not changed or is not Ready for tenor "  + tenor8.getName());
    }

    @Test(description = "Nothing changed for Ignored SPRD model tenor, after changing it's rate", priority = 1)
    public void changeDependenciesForIgnoredSpreadTenorTest() {
        tenor4.setFitFlag(FitFlags.I);
        tenor4.setRateInline("-1.1", true);
        WebDriverHelper.quickSleep();

        Assert.assertEquals(tenor4.getCurrentHouseValueReady(), "-1.10",
                "House value is not changed or is not Ready");
    }
}
