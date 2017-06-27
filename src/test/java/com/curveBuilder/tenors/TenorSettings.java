package com.curveBuilder.tenors;

import com.curveBuilder.utils.ObjectFactory;
import com.curveBuilder.utils.WebDriverHelper;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;

/**
 * Created by anetsman on 2017-06-06.
 */
public class TenorSettings extends ObjectFactory{
    private final static Logger log = Logger.getLogger(TenorSettings.class);

    private WebDriverHelper webDriverHelper;
    private Tenor tenor;
    private TenorModel model;

    private ArrayList<String> marketList;

    @FindBy(id = "")
    private ArrayList<WebElement> marketListElements;
    @FindBy(id = "correspondCheckBoxes")
    private ArrayList<WebElement> correspondCheckBoxes;
    @FindBy(id = "")
    private ArrayList<WebElement> traderModelTabs;
    @FindBy(id = "")
    private WebElement SetStepBtn;
    @FindBy(id = "")
    private WebElement MainCheckBox;

    public TenorSettings(WebDriverHelper  webDriverHelper, Tenor tenor) {
        super(webDriverHelper);
        this.webDriverHelper = webDriverHelper;
        this.tenor = tenor;
        this.model = getCurrentModel();
        marketListElements.forEach(marketName -> marketList.add(marketName.getAttribute("name")));
    }

    public ArrayList<String> getMarketList() {
        return marketList;
    }

    public void setMarkets(ArrayList<String> markets) {
        markets.forEach(market -> {
            if (marketList.contains(market)) {
                try {
                    getCorrespondCheckbox(market).click();
                } catch (ElementNotFoundException e) {
                    log.error(String.format("Market %s is not available for Tenor %s", market, tenor.getName()));
                    e.printStackTrace();
                }
            }
        });
    }

    public TenorModel getModel() {
        return model;
    }

    public void setModel(TenorModel model) {
        this.model = model;
    }

    public WebElement getCorrespondCheckbox(String marketName) {
        return null;
    }

    private TenorModel getCurrentModel() {
        TenorModel tenorModel = null;
        for (WebElement tab:traderModelTabs) {
            if (tab.getAttribute("active").equals("true")) {
                tenorModel = TenorModel.valueOf(tab.getText().toUpperCase());
            }
        }
        return tenorModel;
    }

}
