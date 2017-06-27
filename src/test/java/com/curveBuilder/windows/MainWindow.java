package com.curveBuilder.windows;

import com.curveBuilder.utils.ObjectFactory;
import com.curveBuilder.utils.WebDriverHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by anetsman on 2017-06-06.
 */
public class MainWindow extends ObjectFactory {

    @FindBy(id = "riskGroups")
    private ArrayList<WebElement> riskGroups;
    @FindBy(id = "checkboxes")
    private ArrayList<WebElement> fitFlagsCheckboxes;
    @FindBy(id = "mainOnly")
    private WebElement mainOnlyCheckbox;
    @FindBy(id = "tenorsList")
    private ArrayList<WebElement> tenorsList;

    MainWindow(WebDriverHelper webDriverHelper) {
        super(webDriverHelper);
    }

    public void sortMainOnly(boolean check) {
        boolean checked = false;
        if (mainOnlyCheckbox.getAttribute("checked").equals("checked")) {
            checked = true;
        }

        if (check) {
            if (!checked) {
                mainOnlyCheckbox.click();
            }
        } else {
            if (checked) {
                mainOnlyCheckbox.click();
            }
        }
    }

    public void setFitFlag(String fitFlag) {

    }

    public void sortByFitFlags(List<String> fitFlags) {
        Map<String, Boolean> currentFitFlags = fitFlagsCheckboxes.stream()
                .collect(Collectors.toMap(element -> element.getAttribute("name"),
                        element -> Boolean.valueOf(element.getAttribute("checked"))));

        for (String fitFlag : fitFlags) {
            if (currentFitFlags.containsKey(fitFlag)) {
                if (!currentFitFlags.get(fitFlag)) {
                    fitFlagsCheckboxes.stream()
                            .filter(fitFlagCheckbox -> fitFlagCheckbox.getAttribute("name").equals(fitFlag))
                            .peek(WebElement::click);
                }
            } else throw new IllegalArgumentException(String.format("Fit Flag %s not available", fitFlag));
        }
    }

    public ArrayList<String> getRiskGroups() {
        return riskGroups.stream()
                .map(WebElement::getText)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void switchRiskGroup(String riskGroup) {
        riskGroups.stream()
                .filter(riskgroup -> riskgroup.getAttribute("name").equalsIgnoreCase(riskGroup))
                .peek(WebElement::click);
    }
}
