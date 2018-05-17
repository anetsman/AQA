package com.bondsAutoPricer.utils;

import com.curveBuilder.utils.ConfigParser;
import com.curveBuilder.webElements.MainWindowElements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class WebDriverHelper {
    private static WebDriver driver;
    private ConfigParser parser;

    public WebDriverHelper() {
        this.parser = new ConfigParser();
        System.setProperty("webdriver.chrome.driver", parser.getProperties().getProperty("chromedriver"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--args -allow-file-access-from-files", "disable-extensions", "--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public void start() {
        driver.get(parser.getProperties().getProperty("bondpricer.url"));
        waitForAppBeenLoaded();
    }

    private void waitForAppBeenLoaded() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfElementLocated(MainWindowElements.waitingForConnectionPopUpLocator));

    }
}
