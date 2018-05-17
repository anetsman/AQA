package com.curveBuilder.webElements;

import com.curveBuilder.enums.FitFlags;
import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.windowObjects.BenchmarkButton;
import com.curveBuilder.prototypes.Button;
import com.curveBuilder.utils.WebDriverHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MainWindowElements {

    private static WebDriver driver;

    public static By waitingForConnectionPopUpLocator = By.xpath("//span[contains(., 'Connecting to the server')]");

    /**
     * Curve/Currency selector
     */
    private static String curveSelectorArea = "//div[contains(@class, 'header-selector')]";
    private By curveSelectorLocator = By.xpath(curveSelectorArea + "//input");
    private By currenciesListSelector = By.xpath(curveSelectorArea + "//li");

    private By currencySelector(String currencyName) {
        return By.xpath(String.format("//li[contains(., '%s')]", currencyName));
    }

    /**
     * Risk Groups
     */
    private By riskGroupsLocator = By.xpath("//ul[contains(@class, 'instruments__list')]/li");

    private By riskGroupLocator(String riskGroupName) {
        return By.xpath(String.format(
                "//button[contains(@class, 'instruments-list__button ng-binding') and contains(., '%s')]",
                riskGroupName.toUpperCase()));
    }

    /**
     * Fit flags and Benchmark btn
     */
    private By benchmarkButtonNonActive = By.xpath("//div/label[contains(@class, 'benchmark-btn')][contains(@class, 'non-active')]");
    private By benchmarkButtonActive = By.xpath("//div/label[contains(@class, 'benchmark-btn')][contains(@class, '--active')]");
    private By benchmarkButtonShowOnTop = By.xpath("//div/label[contains(@class, 'benchmark-btn')][contains(@class, 'show-on-top')]");
    private String fitFlagCheckBoxes = "//div[contains(@class, 'fitflag-filters')]/label";

    private By fitFlagCheckBoxLocator(FitFlags fitFlag) {
        return By.xpath(fitFlagCheckBoxes + String.format("[contains(., '%s')]", fitFlag.toString()));
    }
    private By fitFlagCheckBoxesActive = By.xpath(fitFlagCheckBoxes + "[contains(@class, 'active')]");
    private By fitFlagCheckBoxesDisabled = By.xpath(fitFlagCheckBoxes + "[not(contains(@class, 'active'))]");

    /**
     * Global Step Section
     */
    private By setGlobalStepButton = By.xpath("//button[contains(., 'Step')]");
    private By globalStepInput = By.xpath("//input[contains(@ng-keydown, 'SetGlobalStep')]");
    private By globalStepApproveButton = By.xpath("//button[contains(@class, 'button approve__btn_grn')]");
    private By globalStepDeclineButton = By.xpath("button cancel__btn_gry");
    private By applyGlobalStepButton = By.xpath("//button[contains(@class, 'transparent-btn--approve block-btn')]");
    private By cancelGlobalStepButton = By.xpath("//button[contains(@class, 'transparent-btn block-btn')]");

    /**
     * Tenors
     */
    private static String tenors = "//li[contains(@class, 'list-container__item')]";
    public By tenorsListLocator = By.xpath(tenors);

    private By tenorLocatorByName(String tenorName) {
        return By.xpath(String.format(tenors + "//span[text() ='%s']/../../..", tenorName));
    }

    public MainWindowElements() {
        driver = WebDriverHelper.getDriver();
    }

    public WebElement curveSelector() {
        return driver.findElement(curveSelectorLocator);
    }

    public List<WebElement> currencies() {
        return driver.findElements(currenciesListSelector);
    }

    public WebElement currency(String currencyName) {
        return driver.findElement(currencySelector(currencyName));
    }

    public List<WebElement> riskGroups() {
        return driver.findElements(riskGroupsLocator);
    }

    public WebElement riskGroup(String riskGroup) {
        return driver.findElement(riskGroupLocator(riskGroup));
    }

    public Button benchmarkButton() {
        Button benchmarkButton;
        try {
            benchmarkButton = new BenchmarkButton(driver.findElement(benchmarkButtonActive), SwitchStates.ACTIVE);
        } catch (NoSuchElementException e) {
            try {
                benchmarkButton = new BenchmarkButton(driver.findElement(benchmarkButtonNonActive), SwitchStates.NON_ACTIVE);
            } catch (NoSuchElementException er) {
                benchmarkButton = new BenchmarkButton(driver.findElement(benchmarkButtonShowOnTop), SwitchStates.SHOW_ON_TOP);
            }
        }
        return benchmarkButton;
    }

    public List<WebElement> activeFitFlags() {
        return driver.findElements(fitFlagCheckBoxesActive);
    }

    public List<WebElement> disabledFitFlags() {
        return driver.findElements(fitFlagCheckBoxesDisabled);
    }

    public WebElement fitFlagCheckBox(FitFlags fitFlag) {
        return driver.findElement(fitFlagCheckBoxLocator(fitFlag));
    }

    public WebElement setGlobalStepButton() {
        return driver.findElement(setGlobalStepButton);
    }

    public WebElement globalStepInputField() {
        return driver.findElement(globalStepInput);
    }

    public WebElement globalStepApproveButton() {
        return driver.findElement(globalStepApproveButton);
    }

    public WebElement globalStepDeclineButton() {
        return driver.findElement(globalStepDeclineButton);
    }

    public WebElement applyGlobalStepBtn() {
        return driver.findElement(applyGlobalStepButton);
    }

    public WebElement cancelGlobalStepBtn() {
        return driver.findElement(cancelGlobalStepButton);
    }

    public List<WebElement> allTenors() {
        return driver.findElements(tenorsListLocator);
    }

    public List<WebElement> allTenorsNames() {
        return driver.findElements(By.xpath(tenors + "//span[contains(@class, 'tenor__value')]"));
    }

    public WebElement getTenorByName(String tenorName) {
        return driver.findElement(tenorLocatorByName(tenorName));
    }
}
