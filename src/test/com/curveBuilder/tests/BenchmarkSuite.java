package com.curveBuilder.tests;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.utils.RandomizeTenors;
import com.curveBuilder.windowObjects.MainWindow;
import com.curveBuilder.enums.FitFlags;
import com.curveBuilder.windowObjects.Tenor;
import com.curveBuilder.utils.WebDriverHelper;

import org.apache.commons.collections4.ListUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkSuite {

    private static MainWindow mainWindow;
    private static List<String> allTenors;
    private static List<String> tenors = new ArrayList<>();
    private final List<FitFlags> fitFlags= new ArrayList<FitFlags>(){{
        add(FitFlags.F);
        add(FitFlags.O);
        add(FitFlags.S);
        add(FitFlags.I);
    }};

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
        WebDriverHelper webDriverHelper = new WebDriverHelper();
        webDriverHelper.start();
        mainWindow = new MainWindow(webDriverHelper);
        mainWindow.selectCurve("CZK Trader");
        mainWindow.switchToRiskGroup("LIBOR");

        allTenors = mainWindow.getAllTenorsNames();

        RandomizeTenors randomizeTenors = new RandomizeTenors.RandomTenorsBuilder(mainWindow)
                .numberOfTenors(8)
                .fromOneInstrument(false)
                .build();
        tenors = randomizeTenors.getRandomTenors();

        List<Tenor> testTenors = new ArrayList<Tenor>(){{
            tenor1 = new Tenor(tenors.get(0), webDriverHelper);
            tenor2 = new Tenor(tenors.get(1), webDriverHelper);
            tenor3 = new Tenor(tenors.get(2), webDriverHelper);
            tenor4 = new Tenor(tenors.get(3), webDriverHelper);
            tenor5 = new Tenor(tenors.get(4), webDriverHelper);
            tenor6 = new Tenor(tenors.get(5), webDriverHelper);
            tenor7 = new Tenor(tenors.get(6), webDriverHelper);
            tenor8 = new Tenor(tenors.get(7), webDriverHelper);
        }};

        tenor1.setFitFlag(FitFlags.F);
        tenor5.setFitFlag(FitFlags.F);
        tenor2.setFitFlag(FitFlags.O);
        tenor8.setFitFlag(FitFlags.O);
        tenor3.setFitFlag(FitFlags.S);
        tenor7.setFitFlag(FitFlags.S);
        tenor4.setFitFlag(FitFlags.I);
        tenor6.setFitFlag(FitFlags.I);

        testTenors.forEach(tenor -> tenor.benchmarkTenor(true));
    }

    @BeforeMethod
    public void setUp() {
        mainWindow.sortByBenchmark(SwitchStates.ACTIVE);
    }

    @AfterMethod
    public void clear() {
        mainWindow.sortByFitFlags(fitFlags);
    }

    @AfterClass
    public static void closeBrowser() {
        WebDriverHelper.getDriver().close();
        WebDriverHelper.getDriver().quit();
    }

    @Test(description = "sorts tenors by benchmark and check if all of sorted tenors displayed")
    public void sortByBenchmarkTest() {
        Assert.assertEquals(mainWindow.getAllVisibleTenorsNames().size(), tenors.size());

        mainWindow.getAllVisibleTenorsNames()
                .forEach(tenor -> Assert.assertTrue(tenors.contains(tenor), String.format("Tenor %s not in list", tenor)));
    }

    @Test(description = "sorts bencmarked tenors by F fitflag and check if all of sorted tenors displayed")
    public void sortBenchmarkedByFitFlagF() {
        mainWindow.sortByFitFlags(FitFlags.F);

        Assert.assertEquals(mainWindow.getAllVisibleTenorsNames().size(), 2, "Tenors was not sorted or additional FitFlags are added");

        List <String> sortedTenors = new ArrayList<>(mainWindow.getAllVisibleTenorsNames());

        List<String> expectedTenors = new ArrayList<String>() {{
            add(tenor1.getName());
            add(tenor5.getName());
        }};

        Assert.assertEquals(sortedTenors, expectedTenors, String.format("Sorted tenors %s are not expected %s", sortedTenors, expectedTenors));
    }

    @Test(description = "sorts bencmarked tenors by S fitflag and check if all of sorted tenors displayed")
    public void sortBenchmarkedByFitFlagS() {
        mainWindow.sortByFitFlags(FitFlags.S);

        Assert.assertEquals(mainWindow.getAllVisibleTenorsNames().size(), 2, "Tenors was not sorted or additional FitFlags are added");

        List <String> sortedTenors = new ArrayList<>(mainWindow.getAllVisibleTenorsNames());

        List<String> expectedTenors = new ArrayList<String>() {{
            add(tenor3.getName());
            add(tenor7.getName());
        }};

        Assert.assertEquals(sortedTenors, expectedTenors, String.format("Sorted tenors %s are not expected %s", sortedTenors, expectedTenors));
    }

    @Test(description = "sorts bencmarked tenors by O an I fitflags and check if all of sorted tenors displayed")
    public void sortBenchmarkedByFitFlagOandI() {
        mainWindow.sortByFitFlags(new ArrayList<FitFlags>(){{
            add(FitFlags.O);
            add(FitFlags.I);
        }});

        Assert.assertEquals(mainWindow.getAllVisibleTenorsNames().size(), 4, "Tenors was not sorted or additional FitFlags are added");

        List <String> sortedTenors = new ArrayList<>(mainWindow.getAllVisibleTenorsNames());

        List<String> expectedTenors = new ArrayList<String>() {{
            add(tenor2.getName());
            add(tenor4.getName());
            add(tenor6.getName());
            add(tenor8.getName());
        }};

        Assert.assertEquals(sortedTenors, expectedTenors, String.format("Sorted tenors %s are as not expected %s", sortedTenors, expectedTenors));
    }

    @Test(description = "unsorts tenors by benchmark and check if all of sorted tenors displayed", priority = 1)
    public void unsortByBenchmarkTest() {
        mainWindow.sortByBenchmark(SwitchStates.NON_ACTIVE);
        Assert.assertEquals(mainWindow.getAllVisibleTenorsNames().size(), allTenors.size(), "list of visible tenors not equals to the all tenors");
    }

    @Test(description = "show benchmarked tenors on top and check for correcting sorting of tenors", priority = 2)
    public void showOnTopBenchmarkTest() {
        mainWindow.sortByBenchmark(SwitchStates.SHOW_ON_TOP);
        ArrayList sortedTenors = mainWindow.getAllVisibleTenorsNames();

        List<String> expectedTenors = ListUtils.union(tenors, ListUtils.subtract(allTenors, tenors));

        Assert.assertEquals(sortedTenors, expectedTenors, "Sorted Tenors not meet the expectation");
    }
}
