package com.curveBuilder.utils;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.factories.BrokerSwitcherFactory;
import com.curveBuilder.factories.ButtonSwitcherFactory;
import com.curveBuilder.prototypes.SwitcherFactory;
import com.curveBuilder.webElements.MainWindowElements;
import com.curveBuilder.prototypes.Button;
import com.curveBuilder.windowObjects.Tenor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * Created by anetsman on 2017-06-06.
 */
public class WebDriverHelper {

    private static WebDriver driver;
    private ConfigParser parser;

    public WebDriverHelper() {
        this.parser = new ConfigParser();
        parser.configureSession();
        System.setProperty("webdriver.chrome.driver", parser.getProperties().getProperty("chromedriver"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--args --disable-web-security --user-data-dir", "disable-extensions", "--start-maximized");
        options.setExperimentalOption("useAutomationExtension", false);
//        "--args -allow-file-access-from-files", "disable-extensions", "--start-maximized",
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    private void waitForAppBeenLoaded() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfElementLocated(MainWindowElements.waitingForConnectionPopUpLocator));

    }

    public static WebDriver getDriver() {
        return driver;
    }

    public void start() {
        driver.get(parser.getProperties().getProperty("curvebuilder.url"));
        waitForAppBeenLoaded();
    }

    public void switchState(Tenor tenor, Button button, SwitchStates newState) {
        tenor.scrollTo();
        switcher(newState, new ButtonSwitcherFactory(tenor, button));
    }

    public void switchState(Button button, SwitchStates newState, boolean onInstrumentLevel, boolean onRiskGroupLevel) {
        switcher(newState, new BrokerSwitcherFactory(button, onInstrumentLevel, onRiskGroupLevel));
    }

    private void switcher(SwitchStates newState, SwitcherFactory switcherFactory) {
        switcherFactory.getInstance().findStrategyForKey(newState).switchToState();
    }

    public static void quickSleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void longSleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitForElementAppears(By locator) {
        quickSleep();
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForElementAppears(WebElement element) {
        quickSleep();
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementsAppear(By locator) {
        quickSleep();
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public void fillInField(WebElement field, String text) {
        waitForElementAppears(field);
        field.click();
        field.clear();
        field.sendKeys(text);
    }

    public void fillInField(WebElement field, double value) {
        field.clear();
        field.sendKeys(String.valueOf(value));
    }

    public static void performCtrlClick(WebElement element) {
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL)
                .click(element)
                .keyUp(Keys.CONTROL)
                .perform();
    }

    public static void performCtrlShiftClick(WebElement element) {
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL)
                .keyDown(Keys.SHIFT)
                .click(element)
                .keyUp(Keys.CONTROL)
                .keyUp(Keys.SHIFT)
                .perform();
    }

    public void navigateToElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }
}
